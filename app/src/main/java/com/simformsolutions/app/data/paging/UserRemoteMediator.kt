package com.simformsolutions.app.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.simformsolutions.app.data.db.UserDataBase
import com.simformsolutions.app.domain.model.UserRemoteKeys
import com.simformsolutions.app.domain.model.User
import com.simformsolutions.app.domain.remote.apiresult.foldSuspend
import com.simformsolutions.app.domain.repository.UsersRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userDataBase: UserDataBase
): RemoteMediator<Int, User>() {

    private val userDao = userDataBase.userDao()
    private val remoteKeysDao = userDataBase.userRemoteKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        try {
            // Determine the page number based on the load type
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            // Make API request
            val response = usersRepository.loadUsers(currentPage)
            
            val userList = response.foldSuspend(
                onSuccess = { it.results },
                onError = { _, _ -> emptyList() },
                onException = { emptyList() }
            )
            
            val endOfPaginationReached = userList.isEmpty()
            
            userDataBase.withTransaction {
                // Clear all data on refresh
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.deleteAllRemoteKeys()
                    userDao.deleteAllUsers()
                }

                // Calculate prev and next keys
                val prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1
                val nextKey = if (endOfPaginationReached) null else currentPage + 1
                
                // Create remote keys
                val keys = userList.map { user ->
                    UserRemoteKeys(
                        userId = user.login.uuid,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                
                // Insert data into database
                remoteKeysDao.addAllRemoteKeys(keys)
                userDao.insertUsers(userList)
            }
            
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
    
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, User>
    ): UserRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { user ->
                remoteKeysDao.getRemoteKeysById(id = user.login.uuid)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, User>
    ): UserRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { user ->
                remoteKeysDao.getRemoteKeysById(id = user.login.uuid)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, User>
    ): UserRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.login?.uuid?.let { id ->
                remoteKeysDao.getRemoteKeysById(id = id)
            }
        }
    }
}
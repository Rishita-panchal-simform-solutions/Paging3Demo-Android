package com.simformsolutions.app.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.simformsolutions.app.data.db.UserDataBase
import com.simformsolutions.app.data.db.entity.UserRemoteKeys
import com.simformsolutions.app.data.db.entity.UserResponseEntity
import com.simformsolutions.app.domain.model.User
import com.simformsolutions.app.domain.remote.apiresult.ApiResult
import com.simformsolutions.app.domain.remote.response.UserResponse
import com.simformsolutions.app.domain.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

//@OptIn(ExperimentalPagingApi::class)
//class UserRemoteMediator @Inject constructor(
//    private val usersRepository: UsersRepository,
//    private val userDataBase: UserDataBase
//): RemoteMediator<Int, User>() {
//
//    private val userDao = userDataBase.userDao()
//    private val remoteKeysDao = userDataBase.userRemoteKeysDao()
//
//    @ExperimentalPagingApi
//    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
//        return try {
//            val page = when (loadType) {
//                LoadType.REFRESH -> {
//                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                    remoteKeys?.nextKey?.minus(1) ?: 1
//                }
//                LoadType.PREPEND -> {
//                    val remoteKeys = remoteKeysDao.getRemoteKeysById(state.)
//                    val prevKey = remoteKeys?.prevKey
//                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                    prevKey
//                }
//                LoadType.APPEND -> {
//                    val remoteKeys = getRemoteKeyForLastItem(state)
//                    val nextKey = remoteKeys?.nextKey
//                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                    nextKey
//                }
//            }
//
//            val response = usersRepository.loadUsers(page)
//
//            val endOfPaginationReached = when (response) {
//                is ApiResult -> response.data.isEmpty()
//                else -> true
//            }
//
//            withContext(Dispatchers.IO) {
//                if (loadType == LoadType.REFRESH) {
//                    userDao.deleteAllUsers()
//                    remoteKeysDao.deleteAllRemoteKeys()
//                }
//
//                val prevKey = if (page == 1) null else page - 1
//                val nextKey = if (endOfPaginationReached) null else page + 1
//
//                if (response is ApiResult.Success) {
//                    val users = response.data
//                    val userEntities = users.map { it }
//                    val remoteKeys = users.map { user ->
//                        UserRemoteKeys(
//                            id = user.login.uuid,
//                            prevKey = prevKey,
//                            nextKey = nextKey
//                        )
//                    }
//
//                    userDao.insertAll(userEntities)
//                    remoteKeysDao.insertAll(remoteKeys)
//                }
//            }
//
//            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
//        } catch (e: Exception) {
//            MediatorResult.Error(e)
//        }
//    }
//
//    // Extension function to convert User to UserEntity
//    private fun User.toUserEntity() = with(this) {
//        // This is a simplified conversion. Implement according to your actual entity structure
//        com.simformsolutions.app.domain.model.UserEn
//        tity(
//            uuid = login.uuid,
//            // Map other fields as needed
//        )
//    }
//}
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

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userDataBase: UserDataBase
): RemoteMediator<Int, User>() {

    private val userDao = userDataBase.userDao()
    private val remoteKeysDao = userDataBase.userRemoteKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        TODO("Not yet implemented")
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
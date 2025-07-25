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

}
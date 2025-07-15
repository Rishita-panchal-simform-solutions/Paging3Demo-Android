package com.simformsolutions.app.domain.repository

import com.simformsolutions.app.common.dispatcher.IoDispatcher
import com.simformsolutions.app.domain.remote.apiresult.ApiResult
import com.simformsolutions.app.domain.remote.response.UserResponse
import com.simformsolutions.app.domain.remote.service.UserService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val userService: UserService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UsersRepository {
    override suspend fun loadUsers(page: Int): ApiResult<UserResponse> =
        withContext(ioDispatcher) {
            userService.loadUsers(page)
        }
}
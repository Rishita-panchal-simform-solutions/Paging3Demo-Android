package com.simformsolutions.app.domain.repository

import com.simformsolutions.app.domain.remote.apiresult.ApiResult
import com.simformsolutions.app.domain.remote.response.UserResponse
import retrofit2.http.Query

interface UsersRepository {
    suspend fun loadUsers(@Query("page") page: Int): ApiResult<UserResponse>
}

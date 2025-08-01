package com.simformsolutions.app.domain.remote.service

import com.simformsolutions.app.domain.remote.apiresult.ApiResult
import com.simformsolutions.app.domain.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("https://randomuser.me/api/?inc=name,location,picture,login&results=10&seed=abc")
    suspend fun loadUsers(@Query("page") page: Int): ApiResult<UserResponse>
}

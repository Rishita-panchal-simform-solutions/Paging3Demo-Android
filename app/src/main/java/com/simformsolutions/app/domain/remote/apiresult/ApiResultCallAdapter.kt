package com.simformsolutions.app.domain.remote.apiresult

import android.content.Context
import com.simformsolutions.app.domain.remote.network.NetworkMonitor
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Custom [CallAdapter] to adapt call with [responseType] into [ApiResult]
 */
class ApiResultCallAdapter(
    private val responseType: Type,
    private val networkMonitor: NetworkMonitor,
    private val context: Context
) : CallAdapter<Type, Call<ApiResult<Type>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> = ApiResultCall(call, networkMonitor, context)
}

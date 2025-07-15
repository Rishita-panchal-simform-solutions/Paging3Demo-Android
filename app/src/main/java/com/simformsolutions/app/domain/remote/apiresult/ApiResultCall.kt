package com.simformsolutions.app.domain.remote.apiresult

import android.content.Context
import com.simformsolutions.app.R
import com.simformsolutions.app.domain.remote.apiresult.ApiError
import com.simformsolutions.app.domain.remote.apiresult.ApiSuccess
import com.simformsolutions.app.domain.remote.response.BaseResponse
import com.simformsolutions.app.domain.remote.network.NetworkMonitor
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Custom Retrofit call class to delegate response
 *
 * @param call  The retrofit [Call]
 * @param networkMonitor  The [NetworkMonitor] to check network connectivity
 */
class ApiResultCall<T : Any>(
    private val call: Call<T>,
    private val networkMonitor: NetworkMonitor,
    private val context: Context
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        if (!networkMonitor.isOnline) {
            // Return an error result immediately if offline
            val networkResult = ApiException<T>(Throwable(context.getString(R.string.message_no_internet)))
            callback.onResponse(this@ApiResultCall, Response.success(networkResult))
            return
        }

        // Proceed with API call if online
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResult = getApiResult(response)
                callback.onResponse(this@ApiResultCall, Response.success(apiResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResult = ApiException<T>(t)
                callback.onResponse(this@ApiResultCall, Response.success(networkResult))
            }
        })
    }

    override fun execute(): Response<ApiResult<T>> = throw NotImplementedError()
    override fun clone(): Call<ApiResult<T>> = ApiResultCall(call.clone(), networkMonitor, context)
    override fun request(): Request = call.request()
    override fun timeout(): Timeout = call.timeout()
    override fun isExecuted(): Boolean = call.isExecuted
    override fun isCanceled(): Boolean = call.isCanceled
    override fun cancel() { call.cancel() }

    /**
     * Get the [ApiResult] from the [response]
     *
     * @param response  The [Response] received from API call
     *
     * @return [ApiResult.ApiSuccess] if [response] is successful and received body is non-null,
     * Otherwise [ApiResult.ApiError]
     */
    private fun <T : Any> getApiResult(response: Response<T>): ApiResult<T> {
        val body = response.body()
        val errorBody = response.errorBody()
        return if (response.isSuccessful && body != null) {
            ApiSuccess(body)
        } else if (errorBody != null) {
            val errorResponse = Json.decodeFromString<BaseResponse<Unit>>(errorBody.string())
            ApiError(response.code(), errorResponse.message)
        } else {
            ApiError(response.code(), response.message())
        }
    }
}

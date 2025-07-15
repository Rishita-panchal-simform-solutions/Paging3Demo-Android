package com.simformsolutions.app.domain.remote.apiresult

import android.content.Context
import com.simformsolutions.app.domain.remote.network.NetworkMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * Creates [ApiResultCallAdapter] instances based on the return type
 *
 * @param networkMonitor The [NetworkMonitor] to check network connectivity
 */
class ApiResultCallAdapterFactory @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    @ApplicationContext private val context: Context
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java)
            return null

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != ApiResult::class.java)
            return null

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return ApiResultCallAdapter(resultType, networkMonitor, context)
    }
}

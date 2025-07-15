package com.simformsolutions.app.domain.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base API response.
 *
 * @property status `true` if success
 * @property message Error message if any
 * @property data Data
 *
 * Usage:
 *
 *  data class BaseResponse<LoginResponseData>()
 *
 *  data class LoginResponseData(
 *      val userId: String
 *  )
 */
@Serializable
data class BaseResponse<D : Any>(
    @SerialName("status")
    val status: Boolean = false,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: D? = null
)

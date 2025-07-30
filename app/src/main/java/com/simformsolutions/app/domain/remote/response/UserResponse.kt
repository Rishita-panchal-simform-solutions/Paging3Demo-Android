package com.simformsolutions.app.domain.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simformsolutions.app.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity(tableName = "user_response")
@Serializable
data class UserResponse(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    @SerialName("results")
    val results: List<User>,

    @SerialName("info")
    val info: Info
) {
    @Serializable
    data class Info(
        @SerialName("seed")
        val seed: String,

        @SerialName("results")
        val results: Int,

        @SerialName("page")
        val page: Int,

        @SerialName("version")
        val version: String
    )
}

package com.simformsolutions.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simformsolutions.app.domain.model.UserEntity

/**
 * Room DB entity for User response
 */
@Entity(tableName = "user_response")
data class UserResponseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val results: List<UserEntity> = emptyList(),

    val info: InfoEntity = InfoEntity()
) {
    /**
     * Info entity for user response
     */
    @kotlinx.serialization.Serializable
    data class InfoEntity(
        val seed: String = "",
        val results: Int = 0,
        val page: Int = 0,
        val version: String = ""
    )
}
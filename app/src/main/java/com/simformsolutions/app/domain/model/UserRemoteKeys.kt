package com.simformsolutions.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_remote_keys")
data class UserRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
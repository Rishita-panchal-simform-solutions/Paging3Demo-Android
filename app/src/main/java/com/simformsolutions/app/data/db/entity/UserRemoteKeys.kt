package com.simformsolutions.app.data.db.entity

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_remote_keys")
data class UserRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
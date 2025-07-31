package com.simformsolutions.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simformsolutions.app.domain.model.UserRemoteKeys

@Dao
interface UserRemoteKeysDao {
    @Query("SELECT * FROM user_remote_keys WHERE userId = :id")
    suspend fun getRemoteKeysById(id: String): UserRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<UserRemoteKeys>)

    @Query("DELETE FROM user_remote_keys")
    suspend fun deleteAllRemoteKeys()
}
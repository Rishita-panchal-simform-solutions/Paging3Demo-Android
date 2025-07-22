package com.simformsolutions.app.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simformsolutions.app.data.db.entity.UserResponseEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_response")
    fun getAllUsers(): PagingSource<Int, UserResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: UserResponseEntity)

    @Query("DELETE FROM user_response")
    suspend fun deleteAllUsers()
}
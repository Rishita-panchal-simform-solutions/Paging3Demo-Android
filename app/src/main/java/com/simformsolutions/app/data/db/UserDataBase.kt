package com.simformsolutions.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simformsolutions.app.data.db.converters.UserConverters
import com.simformsolutions.app.data.db.dao.UserDao
import com.simformsolutions.app.data.db.dao.UserRemoteKeysDao
import com.simformsolutions.app.domain.model.UserRemoteKeys
import com.simformsolutions.app.domain.model.User

@Database(entities = [
    User::class,
    UserRemoteKeys::class,
], version = 1, exportSchema = false)
@TypeConverters(UserConverters::class)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userRemoteKeysDao(): UserRemoteKeysDao
}
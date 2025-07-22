package com.simformsolutions.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simformsolutions.app.data.db.dao.UserDao
import com.simformsolutions.app.data.db.dao.UserRemoteKeysDao
import com.simformsolutions.app.data.db.entity.UserRemoteKeys
import com.simformsolutions.app.data.db.entity.UserResponseEntity
import com.simformsolutions.app.data.db.typeconverter.Converters

@Database(entities = [
    UserResponseEntity::class,
    UserRemoteKeys::class
], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userRemoteKeysDao(): UserRemoteKeysDao
}
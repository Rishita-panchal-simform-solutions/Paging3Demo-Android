package com.simformsolutions.app.data.db.di

import android.content.Context
import androidx.room.Room
import com.simformsolutions.app.data.db.UserDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): UserDataBase {
        return Room.databaseBuilder(
            context = context,
            UserDataBase::class.java,
            "user_database"
        ).build()
    }
}
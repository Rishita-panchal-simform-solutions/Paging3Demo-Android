package com.simformsolutions.app.domain.remote.network.di

import android.content.Context
import com.simformsolutions.app.domain.remote.network.NetworkMonitor
import com.simformsolutions.app.domain.remote.network.NetworkMonitorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun getNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor =
        NetworkMonitorImpl(context)
}

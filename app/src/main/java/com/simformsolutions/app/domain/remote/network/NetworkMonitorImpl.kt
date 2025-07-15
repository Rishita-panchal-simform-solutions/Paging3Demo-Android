package com.simformsolutions.app.domain.remote.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkMonitor {

    private val validNetworks: MutableList<Network> = mutableListOf()

    private val _isOnlineFlow = MutableSharedFlow<Boolean>(replay = 1)

    override var isOnline: Boolean = false
        private set

    init {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                if (hasInternet(networkCapabilities)) {
                    validNetworks.add(network)
                    updateStatus()
                }
                Timber.d("isOnline -> Status - onAvailable. Internet available ${validNetworks.isNotEmpty()}")
            }

            override fun onLost(network: Network) {
                validNetworks.remove(network)
                updateStatus()
                Timber.d("isOnline -> Status - Lost. Internet available ${validNetworks.isNotEmpty()}")
            }
        }

        val networkRequest = Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (!hasInternet(capabilities)) {
            updateStatus()
        }
        connectivityManager.registerNetworkCallback(networkRequest, callback)
    }

    override val isOnlineFlow: SharedFlow<Boolean> = _isOnlineFlow.asSharedFlow()

    private fun updateStatus(){
        isOnline = validNetworks.isNotEmpty()
        _isOnlineFlow.tryEmit(isOnline)
    }

    private fun hasInternet(networkCapabilities: NetworkCapabilities?): Boolean =
        networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

package com.simformsolutions.app.domain.remote.network

import kotlinx.coroutines.flow.SharedFlow

/**
 * Utility for reporting app connectivity status
 * 
 *  Provides both a [SharedFlow] for observing connectivity changes (`isOnlineFlow`)
 *  and a direct boolean property (`isOnline`) for immediate connectivity checks.
 */
interface NetworkMonitor {
    val isOnlineFlow: SharedFlow<Boolean>
    val isOnline: Boolean
}

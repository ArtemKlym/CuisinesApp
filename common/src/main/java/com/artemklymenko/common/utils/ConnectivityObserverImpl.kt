package com.artemklymenko.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class ConnectivityObserverImpl(
    private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.AVAILABLE)
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.LOST)
            }

            override fun onUnavailable() {
                trySend(NetworkStatus.UNAVAILABLE)
            }
        }

        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}
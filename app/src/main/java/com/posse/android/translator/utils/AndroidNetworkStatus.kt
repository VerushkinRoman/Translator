package com.posse.android.translator.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AndroidNetworkStatus(context: Context) : NetworkStatus {

    private val networkStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        val request = NetworkRequest.Builder().build()
        connectivityManager?.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    networkStatus.value = true
                }

                override fun onUnavailable() {
                    networkStatus.value = false
                }

                override fun onLost(network: Network) {
                    networkStatus.value = false
                }
            })
    }

    override fun getStatus(): StateFlow<Boolean> = networkStatus
}
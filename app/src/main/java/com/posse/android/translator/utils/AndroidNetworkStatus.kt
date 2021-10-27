package com.posse.android.translator.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AndroidNetworkStatus(context: Context) : NetworkStatus {

    private val networkStatus: MutableLiveData<Boolean> = MutableLiveData()

    init {
        networkStatus.value = false
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        val request = NetworkRequest.Builder().build()
        connectivityManager?.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    networkStatus.postValue(true)
                }

                override fun onUnavailable() {
                    networkStatus.postValue(false)
                }

                override fun onLost(network: Network) {
                    networkStatus.postValue(false)
                }
            })
    }

    override fun getStatus(): LiveData<Boolean> = networkStatus
}
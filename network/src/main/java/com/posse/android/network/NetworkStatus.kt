package com.posse.android.network

import kotlinx.coroutines.flow.StateFlow

interface NetworkStatus {
    fun getStatus(): StateFlow<Boolean>
}
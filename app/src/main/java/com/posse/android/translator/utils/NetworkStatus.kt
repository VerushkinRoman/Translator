package com.posse.android.translator.utils

import kotlinx.coroutines.flow.StateFlow

interface NetworkStatus {
    fun getStatus(): StateFlow<Boolean>
}
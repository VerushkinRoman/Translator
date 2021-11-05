package com.posse.android.translator.utils

import androidx.lifecycle.LiveData

interface NetworkStatus {
    fun getStatus(): LiveData<Boolean>
}
package com.posse.android.translator.model.data

import com.posse.android.models.DataModel

sealed class AppState {

    data class Success(val data: List<DataModel>?) : AppState()
    data class Error(val error: Throwable?) : AppState()
    object Loading : AppState()
}
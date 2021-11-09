package com.posse.android.base

import com.posse.android.models.AppState

interface View {

    fun renderData(appState: AppState)
}
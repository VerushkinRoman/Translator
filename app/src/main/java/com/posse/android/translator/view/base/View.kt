package com.posse.android.translator.view.base

import com.posse.android.translator.model.data.AppState

interface View {

    fun renderData(appState: AppState)
}
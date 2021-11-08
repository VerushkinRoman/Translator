package com.posse.android.translator.view.main

import com.posse.android.translator.model.data.AppState
import com.posse.android.translator.presenter.MainInteractor
import com.posse.android.translator.view.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val interactor: MainInteractor
) : BaseViewModel<AppState>() {

    fun getWordDescriptions(word: String, isOnline: Boolean) {
        viewModelCoroutineScope.launch {
            val data = interactor.getData(word, isOnline)
            stateLiveData.value = data
        }
    }

    override fun handleError(error: Throwable) {
        stateLiveData.value = AppState.Error(error)
    }
}
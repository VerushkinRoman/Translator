package com.posse.android.base

import com.posse.android.data.Interactor
import com.posse.android.models.AppState
import kotlinx.coroutines.launch

class MainViewModel(
    private val interactor: Interactor<AppState>
) : BaseViewModel<AppState>() {

    fun getWordDescriptions(word: String, isOnline: Boolean) {
        try {
            viewModelCoroutineScope.launch {
                try {
                    val data = interactor.getData(word, isOnline)
                    stateLiveData.value = data
                } catch (error: Throwable) {
                    handleError(error)
                }
            }
        } catch (error: Throwable) {
            handleError(error)
        }
    }

    override fun handleError(error: Throwable) {
        stateLiveData.value = AppState.Error(error)
    }
}
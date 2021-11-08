package com.posse.android.translator.presenter

import com.posse.android.models.DataModel
import com.posse.android.translator.model.data.AppState
import com.posse.android.data.repository.Repository

class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return if (fromRemoteSource) {
            val data = remoteRepository.getData(word)
            localRepository.saveData(data)
            AppState.Success(data)
        } else {
            val data = localRepository.getData(word)
            AppState.Success(data)
        }
    }
}

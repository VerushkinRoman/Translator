package com.posse.android.data

import com.posse.android.data.repository.Repository
import com.posse.android.models.AppState
import com.posse.android.models.DataModel

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
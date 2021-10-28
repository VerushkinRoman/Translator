package com.posse.android.translator.view.main

import com.posse.android.translator.presenter.Interactor
import com.posse.android.translator.model.data.AppState
import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.repository.Repository
import io.reactivex.rxjava3.core.Observable

class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource) {
            remoteRepository.getData(word).map {
                localRepository.saveData(it)
                AppState.Success(it)
            }
        } else {
            localRepository.getData(word).map { AppState.Success(it) }
        }
    }
}

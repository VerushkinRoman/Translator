package com.posse.android.translator.view.main

import com.posse.android.translator.model.data.AppState
import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.repository.Repository
import com.posse.android.translator.presenter.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MainInteractor @Inject constructor(
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

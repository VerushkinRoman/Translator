package com.posse.android.translator.view.main

import com.posse.android.translator.model.data.AppState
import com.posse.android.translator.model.datasource.DataSourceLocal
import com.posse.android.translator.model.datasource.DataSourceRemote
import com.posse.android.translator.model.repository.RepositoryImplementation
import com.posse.android.translator.presenter.Presenter
import com.posse.android.translator.view.base.View
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class MainPresenterImpl(
    private val interactor: MainInteractor = MainInteractor(
        RepositoryImplementation(DataSourceRemote()),
        RepositoryImplementation(DataSourceLocal())
    ),
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) : Presenter<AppState, View> {

    private var currentView: View? = null

    override fun attachView(view: View) {
        if (view != currentView) {
            currentView = view
        }
    }

    override fun detachView(view: View) {
        compositeDisposable.clear()
        if (view == currentView) {
            currentView = null
        }
    }

    override fun getData(word: String, isOnline: Boolean) {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { currentView?.renderData(AppState.Loading) }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {

            override fun onNext(appState: AppState) {
                currentView?.renderData(appState)
            }

            override fun onError(e: Throwable) {
                currentView?.renderData(AppState.Error(e))
            }

            override fun onComplete() {
            }
        }
    }
}

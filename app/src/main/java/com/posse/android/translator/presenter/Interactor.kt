package com.posse.android.translator.presenter

interface Interactor<T> {

    suspend fun getData(word: String, fromRemoteSource: Boolean): T
}
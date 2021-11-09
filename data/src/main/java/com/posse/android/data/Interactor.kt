package com.posse.android.data

interface Interactor<T> {

    suspend fun getData(word: String, fromRemoteSource: Boolean): T
}
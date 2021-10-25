package com.posse.android.translator.model.repository

import io.reactivex.rxjava3.core.Observable

interface Repository<T> {

    fun getData(word: String): Observable<T>

    fun saveData(dataSet: T)
}

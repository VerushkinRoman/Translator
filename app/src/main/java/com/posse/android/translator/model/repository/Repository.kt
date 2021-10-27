package com.posse.android.translator.model.repository

interface Repository<T> {

    suspend fun getData(word: String): T
    suspend fun saveData(dataSet: T)
}

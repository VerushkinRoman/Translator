package com.posse.android.data.repository

interface Repository<T> {

    suspend fun getData(word: String): T
    suspend fun saveData(dataSet: T)
}

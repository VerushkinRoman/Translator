package com.posse.android.translator.model.datasource

interface DataSource<T> {

    suspend fun getData(word: String): T

    suspend fun saveData(dataSet: T)
}

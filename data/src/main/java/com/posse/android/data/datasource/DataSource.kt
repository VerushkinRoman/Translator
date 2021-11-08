package com.posse.android.data.datasource

interface DataSource<T> {

    suspend fun getData(word: String): T

    suspend fun saveData(dataSet: T)
}

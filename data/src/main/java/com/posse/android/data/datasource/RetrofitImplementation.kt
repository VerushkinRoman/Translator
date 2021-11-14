package com.posse.android.data.datasource

import com.posse.android.models.DataModel

class RetrofitImplementation(private val apiService: ApiService) :
    DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return apiService.search(word)
    }

    override suspend fun saveData(dataSet: List<DataModel>) = Unit
}

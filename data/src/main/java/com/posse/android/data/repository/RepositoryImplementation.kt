package com.posse.android.data.repository

import com.posse.android.models.DataModel
import com.posse.android.data.datasource.DataSource

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun saveData(dataSet: List<DataModel>) {
        dataSource.saveData(dataSet)
    }
}

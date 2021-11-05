package com.posse.android.translator.model.repository

import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.datasource.DataSource

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun saveData(dataSet: List<DataModel>) {
        dataSource.saveData(dataSet)
    }
}

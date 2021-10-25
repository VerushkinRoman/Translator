package com.posse.android.translator.model.repository

import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.datasource.DataSource
import io.reactivex.rxjava3.core.Observable

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        return dataSource.getData(word)
    }

    override fun saveData(dataSet: List<DataModel>) {
        dataSource.saveData(dataSet)
    }
}

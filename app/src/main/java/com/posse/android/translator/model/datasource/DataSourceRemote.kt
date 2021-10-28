package com.posse.android.translator.model.datasource

import com.posse.android.translator.model.data.DataModel
import io.reactivex.rxjava3.core.Observable

class DataSourceRemote(private val remoteProvider: RetrofitImplementation = RetrofitImplementation()) :
    DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)

    override fun saveData(dataSet: List<DataModel>) = remoteProvider.saveData(dataSet)
}

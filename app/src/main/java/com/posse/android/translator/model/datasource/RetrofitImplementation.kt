package com.posse.android.translator.model.datasource

import com.posse.android.translator.model.data.DataModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RetrofitImplementation @Inject constructor(private val apiService: ApiService) :
    DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        return apiService.search(word)
    }

    override fun saveData(dataSet: List<DataModel>) = Unit
}

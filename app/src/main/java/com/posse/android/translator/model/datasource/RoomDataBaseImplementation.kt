package com.posse.android.translator.model.datasource

import com.posse.android.translator.app.App
import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.utils.convertDataModelToRoomModel
import com.posse.android.translator.utils.convertRoomModelToDataModel
import io.reactivex.rxjava3.core.Observable

class RoomDataBaseImplementation : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        return Observable.fromCallable {
            App.db.wordDao.getByWord(word.lowercase())?.map {
                convertRoomModelToDataModel(it)
            }
        }
    }

    override fun saveData(dataSet: List<DataModel>) {
        dataSet.forEach {
            App.db.wordDao.insert(convertDataModelToRoomModel(it))
        }
    }
}

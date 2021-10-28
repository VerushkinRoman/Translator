package com.posse.android.translator.model.datasource

import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.datasource.db.WordsDatabase
import com.posse.android.translator.utils.convertDataModelToRoomModel
import com.posse.android.translator.utils.convertRoomModelToDataModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RoomDataBaseImplementation @Inject constructor(private val db: WordsDatabase) :
    DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        return Observable.fromCallable {
            db.wordDao.getByWord(word.lowercase())?.map {
                convertRoomModelToDataModel(it)
            }
        }
    }

    override fun saveData(dataSet: List<DataModel>) {
        dataSet.forEach {
            db.wordDao.insert(convertDataModelToRoomModel(it))
        }
    }
}

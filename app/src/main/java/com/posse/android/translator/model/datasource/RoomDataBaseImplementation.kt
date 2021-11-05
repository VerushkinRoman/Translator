package com.posse.android.translator.model.datasource

import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.datasource.db.WordsDatabase
import com.posse.android.translator.utils.convertDataModelToRoomModel
import com.posse.android.translator.utils.convertRoomModelToDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataBaseImplementation(private val db: WordsDatabase) :
    DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return withContext(Dispatchers.IO) {
            db.wordDao.getByWord(word.lowercase()).map {
                convertRoomModelToDataModel(it)
            }
        }
    }

    override suspend fun saveData(dataSet: List<DataModel>) {
        withContext(Dispatchers.IO) {
            dataSet.forEach {
                db.wordDao.insert(convertDataModelToRoomModel(it))
            }
        }
    }
}

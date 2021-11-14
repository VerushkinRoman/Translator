package com.posse.android.data.datasource

import com.posse.android.models.DataModel
import com.posse.android.data.datasource.db.WordsDatabase
import com.posse.android.data.convertDataModelToRoomModel
import com.posse.android.data.convertRoomModelToDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataBaseImplementation(private val db: WordsDatabase) :
    DataSource<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return withContext(Dispatchers.IO) {
            if (word == HISTORY) db.wordDao.getAll()
                .filter { it.timestamp > 0 }
                .sortedByDescending { it.timestamp }
                .map {
                convertRoomModelToDataModel(it)
            } else db.wordDao.getByWord(word.lowercase()).map {
                convertRoomModelToDataModel(it)
            }
        }
    }

    override suspend fun saveData(dataSet: List<DataModel>) {
        withContext(Dispatchers.IO) {
            dataSet.forEach {
                val data = convertDataModelToRoomModel(it)
                if (it == dataSet[0]) data.timestamp = System.currentTimeMillis()
                db.wordDao.insert(data)
            }
        }
    }

    companion object {
        const val HISTORY = "Get history"
    }
}

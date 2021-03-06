package com.posse.android.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RoomDataModel::class],
    version = 1
)
abstract class WordsDatabase : RoomDatabase() {

    abstract val wordDao: WordDao
}
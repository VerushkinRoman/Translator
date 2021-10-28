package com.posse.android.translator.di

import android.content.Context
import androidx.room.Room
import com.posse.android.translator.model.datasource.db.WordsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {

    @Provides
    @Singleton
    fun db(context: Context): WordsDatabase {
        return Room.databaseBuilder(context, WordsDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {

        private const val DB_NAME = "database.db"
    }
}
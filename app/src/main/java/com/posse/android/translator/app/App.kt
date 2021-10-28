package com.posse.android.translator.app

import android.app.Application
import androidx.room.Room
import com.posse.android.translator.model.datasource.db.WordsDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        db = Room.databaseBuilder(this, WordsDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {

        private const val DB_NAME = "database.db"

        lateinit var instance: App

        lateinit var db: WordsDatabase
    }
}
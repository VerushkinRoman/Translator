package com.posse.android.translator.app

import android.app.Application
import com.posse.android.translator.di.application
import com.posse.android.translator.di.database
import com.posse.android.translator.di.mainScreen
import com.posse.android.translator.di.network
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(application, mainScreen, network, database))
        }
    }
}
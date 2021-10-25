package com.posse.android.translator.di

import com.posse.android.translator.rx.ISchedulerProvider
import com.posse.android.translator.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SchedulerModule {

    @Provides
    @Singleton
    fun schedulerProvider(): ISchedulerProvider = SchedulerProvider()
}
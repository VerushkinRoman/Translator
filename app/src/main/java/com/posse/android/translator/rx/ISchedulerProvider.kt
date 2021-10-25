package com.posse.android.translator.rx

import io.reactivex.rxjava3.core.Scheduler

interface ISchedulerProvider {

    val ui: Scheduler
    val io: Scheduler
}
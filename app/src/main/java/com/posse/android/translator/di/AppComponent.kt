package com.posse.android.translator.di

import android.app.Application
import com.posse.android.translator.app.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        InteractorModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        RepoModule::class,
        AndroidInjectionModule::class,
        SchedulerModule::class,
        CacheModule::class,
        AppModule::class,
        NetworkModule::class,
    ]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun appModule(appModule: AppModule): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}
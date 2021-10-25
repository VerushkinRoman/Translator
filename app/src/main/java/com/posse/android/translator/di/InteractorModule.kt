package com.posse.android.translator.di

import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.repository.Repository
import com.posse.android.translator.view.main.MainInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class InteractorModule {

    @Provides
    fun provideInteractor(
        @Named(NAME_REMOTE) remoteRepo: Repository<List<DataModel>>,
        @Named(NAME_LOCAL) localRepo: Repository<List<DataModel>>,
    ): MainInteractor {
        return MainInteractor(remoteRepo, localRepo)
    }
}
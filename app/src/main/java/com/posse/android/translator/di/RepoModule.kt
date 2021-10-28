package com.posse.android.translator.di

import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.datasource.ApiService
import com.posse.android.translator.model.datasource.DataSource
import com.posse.android.translator.model.datasource.RetrofitImplementation
import com.posse.android.translator.model.datasource.RoomDataBaseImplementation
import com.posse.android.translator.model.datasource.db.WordsDatabase
import com.posse.android.translator.model.repository.Repository
import com.posse.android.translator.model.repository.RepositoryImplementation
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    fun provideRemoteRepo(@Named(NAME_REMOTE) dataSource: DataSource<List<DataModel>>): Repository<List<DataModel>> {
        return RepositoryImplementation(dataSource)
    }

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    fun provideLocalRepo(@Named(NAME_LOCAL) dataSource: DataSource<List<DataModel>>): Repository<List<DataModel>> {
        return RepositoryImplementation(dataSource)
    }

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideRemoteDataSource(apiService: ApiService): DataSource<List<DataModel>> {
        return RetrofitImplementation(apiService)
    }

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideLocalDataSource(db: WordsDatabase): DataSource<List<DataModel>> {
        return RoomDataBaseImplementation(db)
    }


}
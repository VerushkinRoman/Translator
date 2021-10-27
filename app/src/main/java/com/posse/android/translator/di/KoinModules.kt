package com.posse.android.translator.di

import androidx.room.Room
import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.datasource.ApiService
import com.posse.android.translator.model.datasource.DataSource
import com.posse.android.translator.model.datasource.RetrofitImplementation
import com.posse.android.translator.model.datasource.RoomDataBaseImplementation
import com.posse.android.translator.model.datasource.db.WordsDatabase
import com.posse.android.translator.model.repository.Repository
import com.posse.android.translator.model.repository.RepositoryImplementation
import com.posse.android.translator.utils.AndroidNetworkStatus
import com.posse.android.translator.utils.NetworkStatus
import com.posse.android.translator.view.main.MainInteractor
import com.posse.android.translator.view.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val application = module {

    single<DataSource<List<DataModel>>>(named(NAME_REMOTE)) { RetrofitImplementation(get()) }
    single<Repository<List<DataModel>>>(named(NAME_REMOTE)) {
        RepositoryImplementation(get(named(NAME_REMOTE)))
    }

    single<DataSource<List<DataModel>>>(named(NAME_LOCAL)) { RoomDataBaseImplementation(get()) }
    single<Repository<List<DataModel>>>(named(NAME_LOCAL)) {
        RepositoryImplementation(get(named(NAME_LOCAL)))
    }
}

val mainScreen = module {
    factory { MainInteractor(get(named(NAME_REMOTE)), get(named(NAME_LOCAL))) }
    factory { MainViewModel(get()) }
}

val network = module {
    single<NetworkStatus> { AndroidNetworkStatus(androidContext()) }

    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL_LOCATIONS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<OkHttpClient> {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.build()
    }
}

val database = module {
    single<WordsDatabase> {
        Room.databaseBuilder(androidContext(), WordsDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
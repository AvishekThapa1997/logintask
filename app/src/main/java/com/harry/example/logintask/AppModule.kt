package com.harry.example.logintask

import com.harry.example.logintask.api.Client
import com.harry.example.logintask.repository.AppRepository
import com.harry.example.logintask.utility.NetworkConnectionChecker
import com.harry.example.logintask.viewmodels.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@JvmField
val appModule = module {
    single { getRetrofit() }
    factory { getClient(get()) }
    single { AppRepository(get()) }
    single { NetworkConnectionChecker(androidContext()) }
    viewModel() {
        LoginViewModel(androidApplication(), get())
    }
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder().baseUrl("https://hrm.thedigitalindia.in/")
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun getClient(retrofit: Retrofit): Client {
    return retrofit.create(Client::class.java)
}


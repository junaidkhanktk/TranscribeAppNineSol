package com.example.transcribeapp.client



import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get

import com.example.transcribeapp.client.Keys.token

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.GlobalContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import org.koin.android.ext.android.inject

class RetroFitHelper<T>(
    private val baseUrl: String,
    private val apiService: Class<T>,
    private val connectionTimeOut: Long? = 30,
    private val connectionTimeUnit: TimeUnit? = TimeUnit.SECONDS,
    private val readTimeOut: Long? = 30,
    private val readTimeUnit: TimeUnit? = TimeUnit.SECONDS,
    private val writeTimeOut: Long? = 30,
    private val writeTimeUnit: TimeUnit? = TimeUnit.SECONDS,

    ) {


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build()
    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(connectionTimeOut!!, connectionTimeUnit!!)
            .readTimeout(readTimeOut!!, readTimeUnit!!)
            .writeTimeout(writeTimeOut!!, writeTimeUnit!!)
            .addInterceptor(GlobalContext.get().get<AuthInterceptor>())
            .build()
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val service: T get() = retrofit.create(apiService)

}
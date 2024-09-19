package com.example.transcribeapp.client


import com.example.transcribeapp.client.Keys.token
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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
            .addInterceptor(AuthInterceptor(token))
            .build()
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val service: T get() = retrofit.create(apiService)

}
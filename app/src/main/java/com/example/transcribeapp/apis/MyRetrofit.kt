package com.example.transcribeapp.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ChatRetrofit {
    private val baseUrl="https://chatbotsf-vzt2zxsi7q-uc.a.run.app/"

    private var chatRetrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiService: ApiChatService = chatRetrofit.create(ApiChatService::class.java)
}
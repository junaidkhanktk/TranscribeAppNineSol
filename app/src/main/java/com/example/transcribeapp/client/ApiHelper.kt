package com.example.transcribeapp.client

import com.example.transcribeapp.apis.ApiChatService
import com.example.transcribeapp.authorization.interfaces.AuthService
import com.example.transcribeapp.importAllFile.ImportApiService
import com.example.transcribeapp.summary.ApiSummaryService
import com.example.transcribeapp.summary.TranscribeRequest
import java.security.Key
import java.util.concurrent.TimeUnit

object ApiHelper {

    val chatService by lazy {
        RetroFitHelper(
            baseUrl = Keys.getUlrChat(),
            apiService = ApiChatService::class.java
        ).service
    }

    val summaryService by lazy {
        RetroFitHelper(
            baseUrl = Keys.getSummaryUrl(),
            apiService = ApiSummaryService::class.java
        ).service
    }


    val authService by lazy {
        RetroFitHelper(
            baseUrl = Keys.getAuthUrl(),
            apiService = AuthService::class.java
        ).service
    }


    val transcribeService by lazy {
        RetroFitHelper(
            baseUrl = Keys.getSummaryUrl(),
            apiService = ImportApiService::class.java,
                    connectionTimeOut = 30,
            connectionTimeUnit = TimeUnit.SECONDS,
            readTimeOut = 90,
            readTimeUnit = TimeUnit.SECONDS,
            writeTimeOut = 90,
            writeTimeUnit = TimeUnit.SECONDS
        ).service
    }


}
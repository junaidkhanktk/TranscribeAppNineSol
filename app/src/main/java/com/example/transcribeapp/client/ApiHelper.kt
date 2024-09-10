package com.example.transcribeapp.client

import com.example.transcribeapp.apis.ApiChatService
import com.example.transcribeapp.authorization.interfaces.AuthService
import com.example.transcribeapp.history.server.upload.UploadRecordApiService
import com.example.transcribeapp.summary.ApiSummaryService
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
            baseUrl = Keys.getAuthUrl(),
            apiService = UploadRecordApiService::class.java,
                    connectionTimeOut = 30,
            connectionTimeUnit = TimeUnit.SECONDS,
            readTimeOut = 90,
            readTimeUnit = TimeUnit.SECONDS,
            writeTimeOut = 90,
            writeTimeUnit = TimeUnit.SECONDS
        ).service
    }


}
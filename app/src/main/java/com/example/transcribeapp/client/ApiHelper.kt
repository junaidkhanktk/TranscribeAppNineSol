package com.example.transcribeapp.client

import com.example.transcribeapp.apis.ApiChatService
import com.example.transcribeapp.summary.ApiSummaryService
import java.security.Key

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


}
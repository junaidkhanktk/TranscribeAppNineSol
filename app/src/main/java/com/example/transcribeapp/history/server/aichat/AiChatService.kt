package com.example.transcribeapp.history.aichat

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AiChatService {
    @POST("path/to/your/endpoint")
    suspend fun sendChatRequest(
        @Body chatRequest: AiChatRequestBody,
    ): Response<ResponseBody>
}
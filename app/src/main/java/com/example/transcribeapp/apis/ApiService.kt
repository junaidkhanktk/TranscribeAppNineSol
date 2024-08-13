package com.example.transcribeapp.apis

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiChatService {

        @POST("/chatbot/")
        fun chatRequest(@Body requestBody: RequestBody): Call<ChatResponse>


}
data class ChatResponse(val response: String)
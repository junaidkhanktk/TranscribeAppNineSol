package com.example.transcribeapp.apis

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiChatService {

    @POST("/chatbot/")
    fun chatRequest(@Body requestBody: SimpleChatRequestBody): Call<ChatResponse>


}

data class SimpleChatRequestBody(
    val chatId: String,
    val chatSpecialist: String = "chatbot",
    val prompt: String,
    val is_dummy_response: Boolean = false,
    val chat_directory: String = "chatbot"
)

data class ChatResponse(val message: String)
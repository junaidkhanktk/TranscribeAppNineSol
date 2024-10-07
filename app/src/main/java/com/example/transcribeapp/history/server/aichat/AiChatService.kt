package com.example.transcribeapp.history.server.aichat


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AiChatService {
    @POST("api/aichat/chatBot")
    fun sendChatRequest(
        @Body chatRequest: AiChatRequestBody,
    ): Call<AiChatResponse>
}


interface SuggestionQuestionService {
    @POST("aichat/suggestionQuestion")
    fun sendQuestionRequest(
        @Body questionRequestBody: SuggestionQuestionRequest,
    )
}
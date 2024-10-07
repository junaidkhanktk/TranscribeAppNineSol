package com.example.transcribeapp.history.server.aichat

import com.google.gson.annotations.SerializedName

data class AiChatRequestBody(
    @SerializedName("chatId")
    val chatId: String,
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("recordingId")
    val recordingId: String,
    @SerializedName("eventId")
    val eventId: String?,
)

data class SuggestionQuestionRequest(
    @SerializedName("chatId")
    val chatId: String,
    @SerializedName("recordingId")
    val recordingId: String,
)

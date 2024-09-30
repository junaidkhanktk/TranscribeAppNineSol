package com.example.transcribeapp.history.aichat

import com.google.gson.annotations.SerializedName

data class AiChatRequestBody(
    @SerializedName("chatId")
    val chatId: String,
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("recordingId")
    val recordingId: String,
)

package com.example.transcribeapp.history.server.aichat

import com.google.gson.annotations.SerializedName

data class AiChatResponse(
    @SerializedName("data") val chatData: ChatData?,
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
)

data class ChatData(
    @SerializedName("chatId") val chatId: String,
    @SerializedName("eventId") val eventId: String?,
    @SerializedName("recordingId") val recordingId: String?,
    @SerializedName("suggestion_prompt") val suggestionQuestion: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("prompts") val prompts: List<Prompt>,
)

data class Prompt(
    @SerializedName("_id") val id: String,
    @SerializedName("prompt") val prompt: String,
    @SerializedName("response") val response: String? = null,
    @SerializedName("createdAt") val createdAt: String,
)




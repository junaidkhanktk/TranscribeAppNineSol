package com.example.transcribeapp.history.server.event

import com.google.gson.annotations.SerializedName

data class EventDetailsResponse(
    val success: Boolean,
    val data: Data,
)

data class Data(
    @SerializedName("_id")
    val id: String,
    @SerializedName("recording")
    val recording: String?,
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("keywords")
    val keywords: String,
    @SerializedName("report")
    val report: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("transcribe_text")
    val text: String,
    @SerializedName("aiChat")
    val aiChat: AiChat?=null,
)

data class AiChat(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("chatId")
    val chatId: String,
    @SerializedName("eventId")
    val eventId: String?,
    @SerializedName("recordingId")
    val recordingId: String,
    @SerializedName("chatSpecialist")
    val chatSpecialist: String?,
    @SerializedName("isArchived")
    val isArchived: Boolean,
    @SerializedName("prompts")
    val prompts: List<Prompt>?,
    @SerializedName("__v")
    val v: Int
)


data class Prompt(
    @SerializedName("_id")
    val id: String,
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("response")
    val response: String,
    @SerializedName("createdAt")
    val createdAt: String
)

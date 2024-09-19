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
    val aiChat: String?,
)

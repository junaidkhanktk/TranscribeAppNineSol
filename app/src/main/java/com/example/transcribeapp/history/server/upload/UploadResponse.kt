package com.example.transcribeapp.history.server.upload

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,

    val data: TranscribeData,
)
data class TranscribeData(
    @SerializedName("transcribe_text")
    val text: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("keywords")
    val keywords: String,
    @SerializedName("report")
    val report: String
)
package com.example.transcribeapp.summary

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiSummaryService {
    @POST("/gensummary/")
  fun summaryRequest(
        @Body request: TranscribeRequest,
    ): Call<SummaryResponse>
}

data class TranscribeRequest(
    @SerializedName("transcribe_text")
    val text: String,
)

data class SummaryResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    val data: ResponseData,)

data class ResponseData(
    @SerializedName("summary")
    val summary: String,
)
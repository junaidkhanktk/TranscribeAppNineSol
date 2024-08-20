package com.example.transcribeapp.importAllFile

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface ImportApiService {
    @Multipart
    @POST("/transcribe/")
    fun transcribe(
        @Part file: MultipartBody.Part,
    ): Call<TranscribeResponse>

}

data class TranscribeRequest(
    @SerializedName("audio")
    val audio:File
)

data class TranscribeResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    val data: TranscribeData,
)

data class TranscribeData(
    @SerializedName("transcribe_text")
    val text: String,
)
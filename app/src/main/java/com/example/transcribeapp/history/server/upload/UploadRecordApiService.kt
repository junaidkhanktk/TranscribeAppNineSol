package com.example.transcribeapp.history.server.upload


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadRecordApiService {
    @Multipart
    @POST("/api/recording/upload")
     fun uploadRecording(
        @Part recording: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("conversation") conversation: RequestBody,
        @Part("eventId") eventId: RequestBody,
        @Part("transcribe_text") transcribeText: RequestBody,
    ): Call<UploadResponse>
}


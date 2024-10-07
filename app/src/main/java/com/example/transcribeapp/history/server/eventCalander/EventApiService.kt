package com.example.transcribeapp.history.server.eventCalander

import com.example.transcribeapp.history.server.event.EventDetailsResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface UploadCalenderEventService {
    @POST("api/events/")
    fun uploadCEvent(
        @Body uploadReq: UploadCalanderEventReq,
    ): Call<ResponseBody>
}
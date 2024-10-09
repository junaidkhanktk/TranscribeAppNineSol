package com.example.transcribeapp.calanderEvents.uploadEventCalender

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface UploadCalenderEventService {
    @POST("api/events/")
    fun uploadCEvent(
        @Body uploadReq: UploadCalanderEventReq,
    ): Call<ResponseBody>
}
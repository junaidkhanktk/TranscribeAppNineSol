package com.example.transcribeapp.calanderEvents.eventCalender

import com.example.transcribeapp.history.server.get.RecordingResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface CalenderEventService {
    @POST("api/events/")
    suspend fun uploadCEvent(
        @Body uploadReq: UploadCalenderEventReq,
    ): Response<ResponseBody>

    @GET("api/events/")
    suspend fun getAllEvent(): Response<AllEventResponse>

    @GET("api/events/recordingsData")
    suspend fun getAllRecordingWithEvents(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<RecordingResponse>

}
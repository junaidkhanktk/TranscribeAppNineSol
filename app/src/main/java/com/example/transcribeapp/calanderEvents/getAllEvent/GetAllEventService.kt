package com.example.transcribeapp.calanderEvents.getAllEvent

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface GetAllEventService {

    @GET("api/events/")
    fun getAllEvent(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Call<AllEventResponse>
}
package com.example.transcribeapp.history.server.event

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EventApiService {
    @GET("api/recording/{event-details}/{id}")
    fun getEventDetails(
        @Path("event-details") eventDetails: String,
        @Path("id") id: String
    ): Call<EventDetailsResponse>
}
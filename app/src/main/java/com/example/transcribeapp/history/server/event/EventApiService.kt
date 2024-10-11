package com.example.transcribeapp.history.server.event

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface EventApiService {
    @GET("api/recording/{event-details}/{id}")
    fun getEventDetails(
        @Path("event-details") eventDetails: String,
        @Path("id") id: String
    ): Call<EventDetailsResponse>

   /* @GET
    fun getEventDetails(
        @Url fullUrl: String // Use @Url to pass the full URL dynamically
    ): Call<EventDetailsResponse>*/
}
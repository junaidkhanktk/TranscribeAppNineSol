package com.example.transcribeapp.history.server.get

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GetRecordingApiService {

  /*  @GET
    suspend fun getRecordingsWithoutEvent(
        @Url url: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<RecordingResponse>*/


        @GET("api/recording/null-event")
        suspend fun getRecordingsWithoutEvent(
            @Query("page") page: Int,
            @Query("limit") limit: Int,
        ): Response<RecordingResponse>
}
package com.example.transcribeapp.history.server.get

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetRecordingApiService {

    @GET("api/recording/null-event")
    suspend fun getRecordingsWithoutEvent(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<RecordingResponse>
}
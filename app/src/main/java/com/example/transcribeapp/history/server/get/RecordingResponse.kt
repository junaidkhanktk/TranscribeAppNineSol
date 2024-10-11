package com.example.transcribeapp.history.server.get

import com.google.gson.annotations.SerializedName


//This Response is use same for both Event i.e withEvent and Without Event

data class RecordingResponse(
    @SerializedName("success")
    val success: Boolean,
    val data: RecordingData,
)

data class RecordingData(
    val recordings: MutableList<Recordings>,
    val pagination: Pagination,
)

data class Recordings(
    @SerializedName("_id")
    val id: String,
    @SerializedName("eventId")
    val eventId: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("dateOrTime")
    val dataTime: String,
    @SerializedName("transcribe_text")
    val transcribeTxt: String,
    @SerializedName("timestamp")
    val timeStamp: Long,
)

data class Pagination(
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("totalPages")
    val totalPages: Int

)

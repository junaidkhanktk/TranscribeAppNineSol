package com.example.transcribeapp.calanderEvents.eventCalender

import com.example.transcribeapp.history.server.get.Pagination
import com.google.gson.annotations.SerializedName

data class AllEventResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: EventData
)

data class EventData(
    @SerializedName("events") val events: MutableList<Event>,
    @SerializedName("pagination")   val pagination: Pagination,
)

data class Event(
    @SerializedName("userId") val userId: String,
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("isVisible") val isVisible: Boolean,
    @SerializedName("startTime") val startTime: Long,
    @SerializedName("endTime") val endTime: Long
)

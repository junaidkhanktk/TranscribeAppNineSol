package com.example.transcribeapp.calanderEvents.eventCalender

import com.google.gson.annotations.SerializedName

data class UploadCalenderEventReq(
    @SerializedName("description") val description: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("title") val title: String
)
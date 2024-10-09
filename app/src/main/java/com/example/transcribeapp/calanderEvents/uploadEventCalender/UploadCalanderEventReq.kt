package com.example.transcribeapp.calanderEvents.uploadEventCalender

data class UploadCalanderEventReq(
    val description: String,
    val endTime: String,
    val startTime: String,
    val title: String
)
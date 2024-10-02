package com.example.transcribeapp.history.server.eventCalander

data class UploadCalanderEventReq(
    val description: String,
    val endTime: String,
    val startTime: String,
    val title: String
)
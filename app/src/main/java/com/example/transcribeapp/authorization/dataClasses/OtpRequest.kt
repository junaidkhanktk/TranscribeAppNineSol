package com.example.transcribeapp.authorization.dataClasses

import com.google.gson.annotations.SerializedName

data class OtpRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("otp")
    val otp: String
)
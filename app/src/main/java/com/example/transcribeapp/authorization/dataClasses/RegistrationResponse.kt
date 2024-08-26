package com.example.transcribeapp.authorization.dataClasses

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("user")
    val user: User,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)
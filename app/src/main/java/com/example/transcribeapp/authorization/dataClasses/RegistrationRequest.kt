package com.example.transcribeapp.authorization.dataClasses

import com.google.gson.annotations.SerializedName

data class  RegistrationRequest(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("provider")
    val provider: String,
    @SerializedName("provider_id")
    val providerId: Int,
)
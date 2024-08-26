package com.example.transcribeapp.authorization.dataClasses

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("first_name")
    val firstName: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("email")
    val email: String
)
package com.example.transcribeapp.authorization.dataClasses

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("picture")
    val picture: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("isVerified")
    val isVerified: Boolean,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("token")
    val token: String
)
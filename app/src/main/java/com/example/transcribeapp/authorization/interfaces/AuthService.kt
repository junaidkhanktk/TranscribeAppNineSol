package com.example.transcribeapp.authorization.interfaces

import com.example.transcribeapp.authorization.dataClasses.LoginRequest
import com.example.transcribeapp.authorization.dataClasses.LoginResponse
import com.example.transcribeapp.authorization.dataClasses.OtpRequest
import com.example.transcribeapp.authorization.dataClasses.OtpResponse
import com.example.transcribeapp.authorization.dataClasses.RegistrationRequest
import com.example.transcribeapp.authorization.dataClasses.RegistrationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/api/user/register")
     fun register(@Body request: RegistrationRequest): Call<RegistrationResponse>

    @POST("/api/user/verify-otp")
    fun verifyOtp(@Body request: OtpRequest): Call<OtpResponse>

    @POST("/api/user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
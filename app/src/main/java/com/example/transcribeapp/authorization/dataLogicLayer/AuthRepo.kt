package com.example.transcribeapp.authorization.dataLogicLayer

import android.util.Log
import com.example.transcribeapp.authorization.dataClasses.LoginRequest
import com.example.transcribeapp.authorization.dataClasses.LoginResponse
import com.example.transcribeapp.authorization.dataClasses.OtpRequest
import com.example.transcribeapp.authorization.dataClasses.OtpResponse
import com.example.transcribeapp.authorization.dataClasses.RegistrationRequest
import com.example.transcribeapp.authorization.dataClasses.RegistrationResponse
import com.example.transcribeapp.client.ApiHelper.authService
import com.example.transcribeapp.extension.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepo() {


    suspend fun register(request: RegistrationRequest): Result<RegistrationResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = authService.register(request).execute()
                if (response.isSuccessful) {

                    val isSucces = response.body()!!.success
                    val message=response.body()!!.message

                    if (isSucces){
                        "onResponse Register: ${response.body()!!}".log(Log.DEBUG, "AuthRepo")
                        Result.success(response.body()!!)
                    }else{
                        "onResponse Register else: ${response.body()!!}".log(Log.DEBUG, "AuthRepo")
                        "Registration failed: ${response.errorBody().toString()}".log(Log.DEBUG, "AuthRepo")
                        Result.failure(Exception(message))
                    }


                } else {
                    "onError Register: Registration failed: ${response.message()}".log(
                        Log.DEBUG,
                        "AuthRepo"
                    )
                    Result.failure(Exception("Registration failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                "AuthRepoException Register: Registration Exception: ${e.message}".log(
                    Log.DEBUG,
                    "AuthRepo"
                )
                Result.failure(e)

            }
        }


    suspend fun verifyOtp(request: OtpRequest): Result<OtpResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authService.verifyOtp(request).execute()
            if (response.isSuccessful && response.body() != null) {
                "onResponse verifyOtp: ${response.body()!!}".log(Log.DEBUG, "AuthRepo")
                Result.success(response.body()!!)
            } else {
                "onError Register: Registration failed: ${response.message()}".log(
                    Log.DEBUG,
                    "AuthRepo"
                )
                Result.failure(Exception("verifyOtp failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            "AuthRepoException verifyOtp: verifyOtp Exception: ${e.message}".log(
                Log.DEBUG,
                "AuthRepo"
            )
            Result.failure(e)
        }
    }


    suspend fun login(request: LoginRequest): Result<LoginResponse> = withContext(Dispatchers.IO) {

        try {
            val response = authService.login(request).execute()

            if (response.isSuccessful && response.body() != null) {
                "onResponse login: ${response.body()!!}".log(Log.DEBUG, "AuthRepo")
                Result.success(response.body()!!)

            } else {
                "onError Register: login failed: ${response.message()}".log(
                    Log.DEBUG,
                    "AuthRepo"
                )
                Result.failure(Exception("login failed: ${response.message()}"))
            }

        } catch (e: Exception) {
            "AuthRepoException login: login Exception: ${e.message}".log(
                Log.DEBUG,
                "AuthRepo"
            )
            Result.failure(e)
        }
    }


}
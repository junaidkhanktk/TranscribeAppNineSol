package com.example.transcribeapp.client


import com.example.transcribeapp.client.Keys.token
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.helpers.TinyDB

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.java.KoinJavaComponent.inject


class AuthInterceptor(private val tinyDB: TinyDB) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val authToken = tinyDB.getValue("authToken", "")
        "MyToken---> $authToken".log()

        val request = chain.request()
            .newBuilder()
            .addHeader("Content-type", "multipart/form-data")
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
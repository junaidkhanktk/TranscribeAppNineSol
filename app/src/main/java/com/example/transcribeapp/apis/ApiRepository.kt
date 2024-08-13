package com.example.transcribeapp.apis

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class ApiRepository() {
    fun processChat(userMsg: String, onResponse: (String) -> Unit, onError: (String) -> Unit) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val sanitizedUserMsg = userMsg.replace("\\n", " ").replace("\\r", "")
        val userMsgJson = JSONObject().put("message", sanitizedUserMsg).toString()
        val requestBody = userMsgJson.toRequestBody(mediaType)
        Log.d("Request JSON:", "processChat:$userMsgJson ")


        ChatRetrofit.apiService.chatRequest(requestBody).enqueue(object : retrofit2.Callback<ChatResponse> {
                override fun onResponse(
                    call: Call<ChatResponse>,
                    response: Response<ChatResponse>,
                ) {
                    if (response.isSuccessful) {
                        val replay = response.body()?.response
                        if (replay != null) {
                            onResponse.invoke(replay)
                        } else {
                            onError.invoke("error")

                            Log.d("Request JSON:",  "responseNull:")
                        }


                        Log.d("Request JSON:", "response:$replay")
                    } else {
                        onError.invoke("error")

                        Log.d("Request JSON:", "onErrorResponse:${response.errorBody()?.string()}")
                        Log.d("Request JSON:", "onErrorResponse:${response.errorBody()}")
                        Log.d("Request JSON:", "onErrorResponse:${response.errorBody().toString() }")
                        Log.d("Request JSON:", "onErrorResponse:${response.code()}")

                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {

                    Log.d("Request JSON:",   "onFailure: ${t.message}")
                    onError.invoke("error")
                }

            })


}}






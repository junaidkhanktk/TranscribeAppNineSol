package com.example.transcribeapp.summary

import android.util.Log
import com.example.transcribeapp.client.ApiHelper
import com.example.transcribeapp.extension.log
import retrofit2.Call
import retrofit2.Response

class SummaryRepo {

    fun sendRequest(
        userTxt: String,
        onresPonse: (String) -> Unit,
        onError: (String) -> Unit,

        ) {

        val request = TranscribeRequest(
            text = userTxt
        )


        ApiHelper.summaryService.summaryRequest(request)
            .enqueue(object : retrofit2.Callback<SummaryResponse> {
                override fun onResponse(
                    call: Call<SummaryResponse>,
                    response: Response<SummaryResponse>,
                ) {

                    if (response.isSuccessful) {
                        val status = response.body()?.success
                        val message = response.body()?.message
                        val result = response.body()?.data?.summary
                        result?.let {
                            onresPonse.invoke(it)
                        }


                        "status: ${status} message:${message} result: ${result}  ".log(
                            Log.DEBUG,
                            "Summary"
                        )


                    } else {
                        "request Failed ${response.errorBody().toString()}".log(Log.DEBUG, "Summary")
                    }

                }

                override fun onFailure(call: Call<SummaryResponse>, t: Throwable) {
                    t.message?.let { onError.invoke(it) }
                    "${t.message}".log(Log.DEBUG, "Summary")
                }


            })

    }


}
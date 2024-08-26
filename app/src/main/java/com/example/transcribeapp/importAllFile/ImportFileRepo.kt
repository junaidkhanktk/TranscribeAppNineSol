package com.example.transcribeapp.importAllFile

import android.util.Log
import com.example.transcribeapp.extension.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/*
class ImportFileRepo() {
    private var message: String? = null
    private var result: String? = null
    fun sendRequest(file: File, onResponse: (String) -> Unit, onError: (String) -> Unit) {

        val requestFile = file.asRequestBody("audio/*".toMediaTypeOrNull())
        val audioPart = MultipartBody.Part.createFormData("audio", file.name, requestFile)
        transcribeService.transcribe(audioPart).enqueue(object : Callback<TranscribeResponse>{
            override fun onResponse(
                call: Call<TranscribeResponse>,
                response: Response<TranscribeResponse>,
            ) {
                if (response.isSuccessful) {
                    val status = response.body()?.success
                    message = response.body()?.message
                    result = response.body()?.data?.text
                    result?.let {
                        onResponse.invoke(it)
                    }

                    "status: ${status} message:${message} result: ${result}  ".log(
                        Log.DEBUG,
                        "ImportModule"
                    )

                } else {
                    onError.invoke("error")
                   // "request Failed ${response.errorBody().toString()}".log(Log.DEBUG, "ImportModule")
                    "onErrorResponse:${response.errorBody()?.string()}".log(Log.DEBUG, "ImportModule")
                    "onErrorResponse:${response.errorBody()}".log(Log.DEBUG, "ImportModule")
                    "onErrorResponse:${response.errorBody().toString()}".log(Log.DEBUG, "ImportModule")
                    "onErrorResponse:${response.code()}".log(Log.DEBUG, "ImportModule")
                    "errorBody:${response.body()}".log(Log.DEBUG, "ImportModule")
                }
            }

            override fun onFailure(call: Call<TranscribeResponse>, t: Throwable) {
                onError.invoke("error")
                "${t.message}".log(Log.DEBUG, "ImportModule")
            }

        })

    }

}*/*/



class ImportFileRepo(private val transcribeService: ImportApiService) {
    private val audioMimeType = "audio/*".toMediaTypeOrNull()


    suspend fun sendRequest(file: File): Result<String> = withContext(Dispatchers.IO) {
        try {
            val requestFile = file.asRequestBody(audioMimeType)
            val audioPart = MultipartBody.Part.createFormData("audio",file.name,requestFile)

            val response=transcribeService.transcribe(audioPart).execute()
            if (response.isSuccessful){
                val result=response.body()?.data?.text
                val message=response.body()?.message
                val success=response.body()?.data?.text

                if (result!=null){
                    "onResponse: $result".log(Log.DEBUG, "ImportModule")
                    Result.success(result)

                }else{
                    "onErrorResponse: Transcription result is null".log(Log.DEBUG, "ImportModule")
                    Result.failure(Exception("Transcription result is null"))
                }

            }else{
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                "onError: ${response.code()}, $errorMsg".log(Log.DEBUG, "ImportModule")
                Result.failure(Exception("Error: ${response.code()}, $errorMsg"))

            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }


}

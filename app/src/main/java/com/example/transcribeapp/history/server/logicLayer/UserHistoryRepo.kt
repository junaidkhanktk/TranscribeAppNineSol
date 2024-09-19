package com.example.transcribeapp.history.server.logicLayer

import android.util.Log
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.history.server.event.EventApiService
import com.example.transcribeapp.history.server.event.EventDetailsResponse
import com.example.transcribeapp.history.server.get.GetRecordingApiService
import com.example.transcribeapp.history.server.get.RecordingResponse
import com.example.transcribeapp.history.server.upload.UploadRecordApiService
import com.example.transcribeapp.history.server.upload.UploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserHistoryRepo(
    private val uploadRService: UploadRecordApiService,
    private val getRService: GetRecordingApiService,
    private val getEventService: EventApiService,
) {
    private val audioMimeType = "audio/*".toMediaTypeOrNull()
    suspend fun uploadRecordData(
        title: String,
        conversation: String,
        recordingFile: File,
        eventId: String,
        transcribeText: String,
    ): Result<UploadResponse> = withContext(Dispatchers.IO) {

        try {

            val requestFile = recordingFile.asRequestBody(audioMimeType)
            val audioPart =
                MultipartBody.Part.createFormData("recording", recordingFile.name, requestFile)

            val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val conversationPart =
                conversation.toRequestBody("text/plain".toMediaTypeOrNull())
            val eventIdPart = eventId.toRequestBody("text/plain".toMediaTypeOrNull())
            val transcribeTextPart =
                transcribeText.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = uploadRService.uploadRecording(
                audioPart,
                titlePart,
                conversationPart,
                eventIdPart,
                transcribeTextPart
            ).execute()

            if (response.isSuccessful) {
                val result = response.body()?.data?.text
                val message = response.body()?.message
                val success = response.body()?.data?.text
                if (result != null) {
                    "onResponse: $result".log(Log.DEBUG, "UploadModule")
                    "onResponseBody: ${response.body()!!}".log(Log.DEBUG, "UploadModule")
                    Result.success(response.body()!!)

                } else {
                    "onErrorResponse: Transcription result is null".log(Log.DEBUG, "UploadModule")
                    Result.failure(Exception("Transcription result is null"))
                }
            } else {
                Result.failure(Exception("Failed to upload recording"))
            }
        } catch (e: Exception) {
            val exceptionDetails = Log.getStackTraceString(e)
            "Exception:${e.message}".log(Log.DEBUG, "UploadModule")
            "Exception:${e}".log(Log.DEBUG, "UploadModule")
            "Exception:${exceptionDetails}".log(Log.DEBUG, "UploadModule")
            Result.failure(e)
        }
    }

    suspend fun getRecordData(page: Int, limit: Int): Result<RecordingResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = getRService.getRecordingsWithoutEvent(page, limit)

                if (response.isSuccessful) {
                    val message = response.body()?.success

                    if (message == true) {
                        "onResponseBody: ${response.body()!!}".log(Log.DEBUG, "UploadModule")
                        Result.success(response.body()!!)

                    } else {
                        "onErrorResponse : ${response.errorBody().toString()}".log(
                            Log.DEBUG,
                            "UploadModule"
                        )
                        Result.failure(Exception("recodingResult result is null"))
                    }

                } else {
                    "onErrorResponse not success: ${response.errorBody().toString()}".log(
                        Log.DEBUG,
                        "UploadModule"
                    )

                    "onErrorResponse not success reason : ${response.errorBody()?.string()}".log(
                        Log.DEBUG,
                        "UploadModule"
                    )

                    Result.failure(Exception("Some thing went Wrong Please try again"))
                }
            } catch (e: Exception) {
                "Exception Get Record:${e.message}".log(Log.DEBUG, "UploadModule")
                Result.failure(e)
            } catch (e: HttpException) {
                "HTTP Exception Get Record:${e.message}".log(Log.DEBUG, "UploadModule")
                Result.failure(e)
            }
        }


    suspend fun getEventDetails(eventId: String): Result<EventDetailsResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response =getEventService.getEventDetails("null-event-details",eventId).execute()
                if (response.isSuccessful){
                    val message = response.body()?.success

                    if (message == true) {
                        "onResponseBody: ${response.body()!!}".log(Log.DEBUG, "UploadModule")
                        Result.success(response.body()!!)

                    }else{
                        "onErrorResponse : ${response.errorBody().toString()}".log(
                            Log.DEBUG,
                            "UploadModule"
                        )
                        Result.failure(Exception("EventDetail result is null"))
                    }

                }else{
                    "onErrorResponse not success: ${response.errorBody().toString()}".log(
                        Log.DEBUG,
                        "UploadModule"
                    )
                    Result.failure(Exception("Some thing went Wrong Please try again"))
                }


            } catch (e: Exception) {
                "Exception Get event:${e.message}".log(Log.DEBUG, "UploadModule")
                Result.failure(e)
            } catch (e: HttpException) {
                "HTTP Exception Get Record:${e.message}".log(Log.DEBUG, "UploadModule")
                Result.failure(e)
            }
        }

}



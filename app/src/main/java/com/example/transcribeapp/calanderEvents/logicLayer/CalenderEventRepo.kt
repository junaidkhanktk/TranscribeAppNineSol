package com.example.transcribeapp.calanderEvents.logicLayer

import android.util.Log
import com.example.transcribeapp.calanderEvents.eventCalender.AllEventResponse
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.calanderEvents.eventCalender.UploadCalenderEventReq
import com.example.transcribeapp.calanderEvents.eventCalender.CalenderEventService
import com.example.transcribeapp.history.server.get.RecordingResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CalenderEventRepo(
    private val calenderEventService: CalenderEventService,
) {

    suspend fun upLoadCalenderEvent(request: UploadCalenderEventReq) = withContext(Dispatchers.IO) {
        try {

            val result = calenderEventService.uploadCEvent(request).execute()

            if (result.isSuccessful) {
                val response = result.body()?.string()
                "success:${response}".log(Log.DEBUG, "CalenderEventRepo")
            } else {
                "Error uploading :${result.errorBody()?.string()}".log(Log.DEBUG, "CalenderEventRepo")
                "Error uploading code :${result.code()}".log(Log.DEBUG, "CalenderEventRepo")
            }

        } catch (e: Exception) {
            "Exception:${e.message}".log(Log.DEBUG, "CalenderEventRepo")
        } catch (e: HttpException) {
            "ExceptionHTTP:${e.message}".log(Log.DEBUG, "CalenderEventRepo")
        }

    }

    suspend fun getAllCalenderEvent(page: Int, limit: Int): Result<AllEventResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = calenderEventService.getAllEvent (page, limit)

                if (response.isSuccessful) {
                    val message = response.body()?.success

                    if (message == true) {
                        "onResponseBody: ${response.body()!!}".log(Log.DEBUG, "CalenderEventRepo")
                        Result.success(response.body()!!)

                    } else {
                        "onErrorResponse : ${response.errorBody().toString()}".log(
                            Log.DEBUG,
                            "CalenderEventRepo"
                        )
                        Result.failure(Exception("Event result is null"))
                    }

                } else {
                    "onErrorResponse not success: ${response.errorBody().toString()}".log(
                        Log.DEBUG,
                        "CalenderEventRepo"
                    )

                    "onErrorResponse not success reason : ${response.errorBody()?.string()}".log(
                        Log.DEBUG,
                        "CalenderEventRepo"
                    )

                    Result.failure(Exception("Some thing went Wrong Please try again"))
                }
            } catch (e: Exception) {
                "Exception Get allEvent:${e.message}".log(Log.DEBUG, "CalenderEventRepo")
                Result.failure(e)
            } catch (e: HttpException) {
                "HTTP Exception Get allEvent:${e.message}".log(Log.DEBUG, "CalenderEventRepo")
                Result.failure(e)
            }
        }


}
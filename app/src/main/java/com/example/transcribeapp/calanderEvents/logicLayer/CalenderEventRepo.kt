package com.example.transcribeapp.calanderEvents.logicLayer

import android.util.Log
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.calanderEvents.uploadEventCalender.UploadCalanderEventReq
import com.example.transcribeapp.calanderEvents.uploadEventCalender.UploadCalenderEventService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CalenderEventRepo(
    private val uploadCEventService: UploadCalenderEventService,
) {

    suspend fun upLoadCalenderEvent(request: UploadCalanderEventReq) = withContext(Dispatchers.IO) {
        try {

            val result = uploadCEventService.uploadCEvent(request).execute()

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

}
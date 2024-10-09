package com.example.transcribeapp.calanderEvents.logicLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transcribeapp.calanderEvents.uploadEventCalender.UploadCalanderEventReq
import kotlinx.coroutines.launch

class CalenderEventViewModel(private val repo: CalenderEventRepo) : ViewModel() {


    fun upLoadCalenderEvent(request: UploadCalanderEventReq) = viewModelScope.launch{
        repo.upLoadCalenderEvent(request)
    }


}
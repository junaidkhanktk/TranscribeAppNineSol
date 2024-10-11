package com.example.transcribeapp.calanderEvents.logicLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.transcribeapp.calanderEvents.eventCalender.AllEventResponse
import com.example.transcribeapp.calanderEvents.eventCalender.Event
import com.example.transcribeapp.calanderEvents.eventCalender.UploadCalenderEventReq
import com.example.transcribeapp.history.server.get.Recordings
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalenderEventViewModel(private val repo: CalenderEventRepo) : ViewModel() {

    private val _allCEvent = MutableStateFlow<UiState<AllEventResponse?>>(UiState.Idle)
    val allCEvent = _allCEvent.asStateFlow()


    private var currentPagingSource: RecordingsWithEventPagingSource? = null

    private fun getNewPagingSource(): PagingSource<Int, Recordings> {
        return RecordingsWithEventPagingSource(repo).also { currentPagingSource = it }
    }

    private fun inValidatePagingSource() {
        currentPagingSource?.invalidate()
    }


    val recordingWithEvent = Pager(PagingConfig(pageSize = 10)) {
        getNewPagingSource()
    }.flow.cachedIn(viewModelScope)


    fun upLoadCalenderEvent(request: UploadCalenderEventReq) = viewModelScope.launch {
        val result = repo.upLoadCalenderEvent(request)
        if (result.isSuccess){
            getAllCalenderEvent()
        }
    }


    fun getAllCalenderEvent() = viewModelScope.launch(Dispatchers.IO) {
        _allCEvent.emit(UiState.Loading)
        val result = repo.getAllCalenderEvent()
        if (result.isSuccess) {
            _allCEvent.emit(UiState.Success(result.getOrNull()))
        } else {
            _allCEvent.emit(
                UiState.Error(
                    result.exceptionOrNull()?.message ?: "An Unknown error occurred"
                )
            )
        }


    }


}
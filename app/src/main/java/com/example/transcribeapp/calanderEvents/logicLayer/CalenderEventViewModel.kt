package com.example.transcribeapp.calanderEvents.logicLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.transcribeapp.calanderEvents.eventCalender.Event
import com.example.transcribeapp.calanderEvents.eventCalender.UploadCalenderEventReq
import kotlinx.coroutines.launch

class CalenderEventViewModel(private val repo: CalenderEventRepo) : ViewModel() {


    private var currentPagingSource: AllEventPagingSource? = null

    private fun getNewPagingSource(): PagingSource<Int, Event> {
        return AllEventPagingSource(repo).also { currentPagingSource = it }

    }

    private fun inValidatePagingSource() {
        currentPagingSource?.invalidate()
    }


    val allCEvent = Pager(PagingConfig(pageSize = 10)) {
        getNewPagingSource()
    }.flow.cachedIn(viewModelScope)


    fun upLoadCalenderEvent(request: UploadCalenderEventReq) = viewModelScope.launch {
        repo.upLoadCalenderEvent(request)
    }


}
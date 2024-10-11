package com.example.transcribeapp.history.server.logicLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.transcribeapp.history.server.aichat.AiChatRequestBody
import com.example.transcribeapp.history.server.aichat.AiChatResponse
import com.example.transcribeapp.history.server.event.EventDetailsResponse
import com.example.transcribeapp.history.server.get.RecordingResponse
import com.example.transcribeapp.history.server.get.Recordings
import com.example.transcribeapp.history.server.upload.UploadResponse
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class UserHistoryViewModel(private val repo: UserHistoryRepo) : ViewModel() {

    private val _currentUrl = MutableStateFlow("")

    fun setUrl(url: String) {
        _currentUrl.value = url
    }

    private
    val _uploadResult = MutableStateFlow<UiState<UploadResponse?>>(UiState.Idle)
    val uploadResult = _uploadResult.asStateFlow()

    private val _recodingResult = MutableStateFlow<UiState<RecordingResponse?>>(UiState.Idle)
    val recodingResult = _recodingResult.asStateFlow()

    private val _eventDetailResult = MutableStateFlow<UiState<EventDetailsResponse?>>(UiState.Idle)
    val eventDetailResult = _eventDetailResult.asStateFlow()

    private val _aiChatResult = MutableStateFlow<UiState<AiChatResponse?>>(UiState.Idle)
    val aiChatResult = _aiChatResult.asStateFlow()

    private var currentPagingSource: RecordingsPagingSource? = null


   /* private fun getNewPagingSource(url: String): PagingSource<Int, Recordings> {
        return RecordingsPagingSource(repo, url).also { currentPagingSource = it }
    }*/



    private fun getNewPagingSource(): PagingSource<Int, Recordings> {
        return RecordingsPagingSource(repo).also { currentPagingSource = it }
    }

    val recordingsWithoutEvent = Pager(PagingConfig(pageSize = 10)) {
        getNewPagingSource()
    }.flow.cachedIn(viewModelScope)

  /*  val recordingsFlow: Flow<PagingData<Recordings>> = _currentUrl
        .filter { it.isNotEmpty() }
        .flatMapLatest { url->
            Pager(PagingConfig(pageSize = 10)){
                getNewPagingSource(url)
            }.flow.cachedIn(viewModelScope)
        }*/


    private fun invalidatePagingSource() {
        currentPagingSource?.invalidate()
    }

    /*  fun upLoadCalenderEvent(request: UploadCalanderEventReq) = viewModelScope.launch{
          repo.upLoadCalenderEvent(request)
      }*/

    fun uploadRecordData(
        title: String,
        conversation: String,
        recordingFile: File,
        eventId: String,
        transcribeText: String,
    ) = viewModelScope.launch {
        _uploadResult.value = UiState.Loading

        val result =
            repo.uploadRecordData(title, conversation, recordingFile, eventId, transcribeText)
        if (result.isSuccess) {
            invalidatePagingSource()
            _uploadResult.value = UiState.Success(result.getOrNull())
        } else {
            _uploadResult.value =
                UiState.Error(result.exceptionOrNull()?.message ?: "An Unknown error occurred")
        }

    }

    fun getWithoutEventDetails(eventDetails: String, id: String) = viewModelScope.launch {
        _eventDetailResult.value = UiState.Loading
        val result = repo.getWithoutEventDetails(eventDetails, id)
        if (result.isSuccess) {
            _eventDetailResult.value = UiState.Success(result.getOrNull())
        } else {
            _eventDetailResult.value =
                UiState.Error(result.exceptionOrNull()?.message ?: "An Unknown error occurred")
        }
    }


    fun aiChat(chatReq: AiChatRequestBody) = viewModelScope.launch {
        _aiChatResult.value = UiState.Loading
        val result = repo.aiChat(chatReq)
        if (result.isSuccess) {
            _aiChatResult.value = UiState.Success(result.getOrNull())
        } else {
            _aiChatResult.value =
                UiState.Error(result.exceptionOrNull()?.message ?: "An Unknown error occurred")

        }
    }


}
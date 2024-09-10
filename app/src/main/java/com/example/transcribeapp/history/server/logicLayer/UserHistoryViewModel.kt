package com.example.transcribeapp.history.server.logicLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.history.server.get.RecordingResponse
import com.example.transcribeapp.history.server.get.Recordings
import com.example.transcribeapp.history.server.upload.UploadResponse
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class UserHistoryViewModel(private val repo: UserHistoryRepo) : ViewModel() {
    private var currentPage = 1
    private var totalPages = 1
    var isLoading = false
    private val _uploadResult = MutableStateFlow<UiState<UploadResponse?>>(UiState.Idle)
    val uploadResult = _uploadResult.asStateFlow()

    private val _recodingResult = MutableStateFlow<UiState<RecordingResponse?>>(UiState.Idle)
    val recodingResult = _recodingResult.asStateFlow()
   // private var accumulatedRecordingResponse: RecordingResponse? = null


 /*   val recordingsFlow = Pager(PagingConfig(pageSize = 10)) {
        RecordingsPagingSource(repo)
    }.flow.cachedIn(viewModelScope)*/

    private var currentPagingSource: RecordingsPagingSource? = null


    private fun getNewPagingSource(): PagingSource<Int, Recordings> {
        return RecordingsPagingSource(repo).also { currentPagingSource = it }
    }

    val recordingsFlow = Pager(PagingConfig(pageSize = 10)) {
        getNewPagingSource()
    }.flow.cachedIn(viewModelScope)



    private fun invalidatePagingSource() {
        currentPagingSource?.invalidate()
    }
    fun uploadRecordData(
        title: String,
        conversation: String,
        recordingFile: File,
        eventId: String,
        transcribeText: String,
    ) = viewModelScope.launch(Dispatchers.IO) {

        _uploadResult.value = UiState.Loading

        val result =
            repo.uploadRecordData(title, conversation, recordingFile, eventId, transcribeText)
        if (result.isSuccess) {
            _uploadResult.value = UiState.Success(result.getOrNull())
        } else {
            invalidatePagingSource()
            _uploadResult.value =
                UiState.Error(result.exceptionOrNull()?.message ?: "An Unknown error occurred")
        }

    }


    fun getRecordData(page: Int = 1, limit: Int = 10) = viewModelScope.launch {

        if (isLoading || page > totalPages) return@launch

        _recodingResult.value = UiState.Loading
        isLoading = true
        val result = repo.getRecordData(page, limit)
        if (result.isSuccess) {
            result.getOrNull()?.let { recordingResponse ->
                /*val currentRecordings =
                    accumulatedRecordingResponse?.data?.recordings ?: mutableListOf()

                currentRecordings.addAll(recordingResponse.data.recordings)

                accumulatedRecordingResponse = recordingResponse.copy(
                    data = recordingResponse.data.copy(
                        recordings = currentRecordings
                    )
                )*/
                _recodingResult.value = UiState.Success(recordingResponse)

                currentPage = recordingResponse.data.pagination.page
                totalPages = recordingResponse.data.pagination.totalPages
            }
        } else {
            _recodingResult.value =
                UiState.Error(result.exceptionOrNull()?.message ?: "An Unknown error occurred")
        }

        isLoading = false

    }

    fun loadNextPage() {
        if (currentPage < totalPages) {
            "nextPageload ViewModel".log()
            getRecordData(currentPage + 1)
        }
    }


}
package com.example.transcribeapp.importAllFile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ImportViewModel(private val repo: ImportFileRepo) : ViewModel() {

    private val _transcribeResponse = MutableStateFlow<UiState<String>>(UiState.Idle)
    val transcribeResponse=_transcribeResponse.asStateFlow()
    fun sendRequest(file: File) = viewModelScope.launch {

        _transcribeResponse.value = UiState.Loading
        val result = repo.sendRequest(file)
        if (result.isSuccess) {
            _transcribeResponse.value = UiState.Success(result.getOrNull() ?: "")
        } else {
            _transcribeResponse.value = UiState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
        }
    }

}
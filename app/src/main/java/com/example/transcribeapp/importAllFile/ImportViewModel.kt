package com.example.transcribeapp.importAllFile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ImportViewModel(private val repo: ImportFileRepo) : ViewModel() {



    private val _transcribeResponse = MutableStateFlow<String?>(null)
    val transcribeResponse=_transcribeResponse.asStateFlow()


    fun sendRequest(file: File) = viewModelScope.launch(Dispatchers.IO) {
        repo.sendRequest(file, onResponse = {
            _transcribeResponse.value=it
        }, onError = {})

    }
}
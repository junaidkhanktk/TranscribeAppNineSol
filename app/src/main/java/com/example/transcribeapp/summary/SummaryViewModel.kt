package com.example.transcribeapp.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SummaryViewModel(private val repo: SummaryRepo) : ViewModel() {


    private val _summaryResponse = MutableStateFlow<String?>(null)
    val summaryResponse=_summaryResponse.asStateFlow()


    fun sendRequest(userTxt: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.sendRequest(userTxt, onResponse = {
            _summaryResponse.value=it
        }, onError = {})

    }


}
package com.example.transcribeapp.history.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.history.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch


class HistoryViewModel(private val historyRepo: HistoryRepo) :
    ViewModel() {



    val readHistory: Flow<List<History>> = historyRepo.readHistory

    private val _selectedItemHistory = MutableStateFlow<History?>(null)
    val selectedItemHistory = _selectedItemHistory.asStateFlow()

    fun setSelectedItemHistory(history: History) {
        _selectedItemHistory.value = history
    }

    fun insertHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        try {
            historyRepo.insertHistory(history)
        } catch (e: OutOfMemoryError) {
            "DatabaseError-> Out of memory during database operation: cause ${e.cause} message${e.message}".log(
                Log.ERROR,
                "DataBase"
            )
        }

    }

    fun deleteHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        historyRepo.deleteHistory(history)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


}



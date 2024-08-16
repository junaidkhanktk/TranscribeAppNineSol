package com.example.transcribeapp.history.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.history.History
import com.example.transcribeapp.history.HistoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class HistoryViewModel(private val historyDao: HistoryDao) :
    ViewModel() {
    val readHistory: Flow<List<History>> = historyDao.readHistory()
    private val _selectedItemHistory = MutableStateFlow<History?>(null)
    val selectedItemHistory = _selectedItemHistory.asStateFlow()

    fun setSelectedItemHistory(history: History) {
        _selectedItemHistory.value = history
    }

    fun insertHistory(history: History)  {
        try {
            historyDao.insertHistory(history)
            "history Called in ViewModel".log()
        } catch (e: OutOfMemoryError) {
            "DatabaseError-> Out of memory during database operation: cause ${e.cause} message${e.message}".log(
                Log.ERROR,
                "DataBase"
            )
        }

    }

    fun deleteHistory(history: History)  {
        historyDao.deleteHistory(history)
    }

    override fun onCleared() {
        super.onCleared()

    }


}



package com.example.transcribeapp.history.mvvm

import androidx.lifecycle.LiveData
import com.example.transcribeapp.history.History
import com.example.transcribeapp.history.HistoryDao
import kotlinx.coroutines.flow.Flow


class HistoryRepo(
    private val historyDao: HistoryDao,
) {

    val readHistory: Flow<List<History>> = historyDao.readHistory()


    fun insertHistory(history: History) {
        historyDao.insertHistory(history)
    }

    fun deleteHistory(history: History) {
        historyDao.deleteHistory(history)
    }

}
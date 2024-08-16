package com.example.transcribeapp.history.mvvm

import com.example.transcribeapp.extension.log
import com.example.transcribeapp.history.History
import com.example.transcribeapp.history.HistoryDao
import kotlinx.coroutines.flow.Flow


class HistoryRepo(
    private val historyDao: HistoryDao,
) {

    val readHistory: Flow<List<History>> = historyDao.readHistory()


    fun insertHistory(history: History) {
            historyDao.insertHistory(history)
            "history Called in Repo".log()
    }

    fun deleteHistory(history: History) {
        historyDao.deleteHistory(history)
    }

}
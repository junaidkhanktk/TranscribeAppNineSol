package com.example.transcribeapp.koin

import com.example.transcribeapp.apis.ApiRepository
import com.example.transcribeapp.apis.ChatViewModel
import com.example.transcribeapp.history.HistoryDao
import com.example.transcribeapp.history.HistoryDataBase
import com.example.transcribeapp.history.HistoryDataBase.Companion.getDataBase
import com.example.transcribeapp.history.mvvm.HistoryRepo
import com.example.transcribeapp.history.mvvm.HistoryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import kotlin.math.sin

val allModules = module {
    single { ApiRepository() }
    single { ChatViewModel(repo = get()) }
    single {getDataBase(get()).historyDao() }
    single { HistoryRepo(get()) }
    single { HistoryViewModel(historyRepo = get()) }
    single { HistoryRepo(historyDao = get()) }
}

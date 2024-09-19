package com.example.transcribeapp.koin

import com.example.transcribeapp.apis.ApiRepository
import com.example.transcribeapp.apis.ChatViewModel
import com.example.transcribeapp.authorization.dataLogicLayer.AuthRepo
import com.example.transcribeapp.authorization.dataLogicLayer.AuthViewModel
import com.example.transcribeapp.authorization.google.GoogleSignInViewModel
import com.example.transcribeapp.authorization.interfaces.AuthService
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.client.RetroFitHelper
import com.example.transcribeapp.databinding.FragmentConversationBinding
import com.example.transcribeapp.fragment.ConversationFragment
import com.example.transcribeapp.history.HistoryDataBase.Companion.getDataBase
import com.example.transcribeapp.history.mvvm.HistoryRepo
import com.example.transcribeapp.history.mvvm.HistoryViewModel
import com.example.transcribeapp.history.server.event.EventApiService
import com.example.transcribeapp.history.server.get.GetRecordingApiService
import com.example.transcribeapp.history.server.upload.UploadRecordApiService
import com.example.transcribeapp.history.server.logicLayer.UserHistoryRepo
import com.example.transcribeapp.history.server.logicLayer.UserHistoryViewModel
import com.example.transcribeapp.importAllFile.ImportApiService
import com.example.transcribeapp.importAllFile.ImportFileRepo
import com.example.transcribeapp.importAllFile.ImportViewModel
import com.example.transcribeapp.recorder.AudioRecorderManager
import com.example.transcribeapp.recorder.SpeechRecognitionManager
import com.example.transcribeapp.summary.SummaryRepo
import com.example.transcribeapp.summary.SummaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val allModules = module {

    single { getDataBase(get()).historyDao() }

    single { (fragment: ConversationFragment) ->
        SpeechRecognitionManager(
            context = get(),
            recognitionListener = fragment
        )
    }

    factory { (binding: FragmentConversationBinding) ->
        AudioRecorderManager(
            context = get(),
            binding
        )
    }

    single { SpeechRecognitionManager(context = get(), recognitionListener = get()) }
}

val networkModule = module {
    single {
        RetroFitHelper(
            baseUrl = Keys.getSummaryUrl(),
            apiService = ImportApiService::class.java,
            readTimeOut = 90,
            readTimeUnit = TimeUnit.SECONDS,
            writeTimeOut = 90,
            writeTimeUnit = TimeUnit.SECONDS
        ).service
    }

    single {
        RetroFitHelper(
            baseUrl = Keys.getAuthUrl(),
            apiService = AuthService::class.java
        ).service
    }

    single {
        RetroFitHelper(
            baseUrl = Keys.getAuthUrl(),
            apiService = UploadRecordApiService::class.java,
            readTimeOut = 90,
            readTimeUnit = TimeUnit.SECONDS,
            writeTimeOut = 90,
            writeTimeUnit = TimeUnit.SECONDS
        ).service
    }

    single {
        RetroFitHelper(
            baseUrl = Keys.getAuthUrl(),
            apiService = GetRecordingApiService::class.java
        ).service
    }
    single {
        RetroFitHelper(
            baseUrl = Keys.getAuthUrl(),
            apiService = EventApiService::class.java
        ).service
    }


}

val viewModelModule = module {
    single { HistoryRepo(get()) }
    single { HistoryViewModel(historyDao = get()) }
    single { ApiRepository() }
    single { ChatViewModel(repo = get()) }

    single { ImportFileRepo(get()) }
    single { ImportViewModel(repo = get()) }

    // single { HistoryRepo(historyDao = get()) }

    single { SummaryRepo() }
    single { SummaryViewModel(repo = get()) }

    single { AuthRepo() }
    single { AuthViewModel(get()) }

    viewModel { GoogleSignInViewModel(get()) }
    single { UserHistoryRepo(uploadRService = get(), getRService = get(), getEventService = get()) }
    single { UserHistoryViewModel(repo = get()) }

}

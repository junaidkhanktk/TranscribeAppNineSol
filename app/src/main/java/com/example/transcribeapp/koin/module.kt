package com.example.transcribeapp.koin

import androidx.credentials.CredentialManager
import com.example.transcribeapp.apis.ApiRepository
import com.example.transcribeapp.apis.ChatViewModel
import com.example.transcribeapp.authorization.dataLogicLayer.AuthRepo
import com.example.transcribeapp.authorization.dataLogicLayer.AuthViewModel
import com.example.transcribeapp.authorization.google.GoogleSignInRepo
import com.example.transcribeapp.authorization.google.GoogleSignInRepository
import com.example.transcribeapp.authorization.google.GoogleSignInViewModel
import com.example.transcribeapp.authorization.interfaces.AuthService
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.client.RetroFitHelper
import com.example.transcribeapp.databinding.FragmentConversationBinding
import com.example.transcribeapp.fragment.ConversationFragment
import com.example.transcribeapp.history.HistoryDataBase.Companion.getDataBase
import com.example.transcribeapp.history.mvvm.HistoryRepo
import com.example.transcribeapp.history.mvvm.HistoryViewModel
import com.example.transcribeapp.importAllFile.ImportApiService
import com.example.transcribeapp.importAllFile.ImportFileRepo
import com.example.transcribeapp.importAllFile.ImportViewModel
import com.example.transcribeapp.recorder.AudioRecorderManager
import com.example.transcribeapp.recorder.SpeechRecognitionManager
import com.example.transcribeapp.summary.SummaryRepo
import com.example.transcribeapp.summary.SummaryViewModel
import com.example.transcribeapp.utils.Constants
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
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

val networkModule= module {
    single {
        RetroFitHelper(
            baseUrl = Keys.getSummaryUrl(),
            apiService = ImportApiService::class.java,
            connectionTimeOut = 30,
            connectionTimeUnit = TimeUnit.SECONDS,
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

}


val viewModelModule = module{
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

    single { CredentialManager.create(get()) }

    single {
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(Constants.GOOGLE_SIGNIN_KEY)
            .setAutoSelectEnabled(false)
            .build()
    }
    single<GoogleSignInRepository> {
        GoogleSignInRepo(get(), get(), get())
    }

    /*single<GoogleSignInRepo> {
        GoogleSignInRepo(get(), get(), get())
    }*/

    viewModel { GoogleSignInViewModel(get()) }

}

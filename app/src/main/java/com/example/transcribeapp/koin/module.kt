package com.example.transcribeapp.koin

import com.example.transcribeapp.apis.ApiRepository
import com.example.transcribeapp.apis.ChatViewModel
import com.example.transcribeapp.databinding.FragmentConversationBinding
import com.example.transcribeapp.fragment.ConversationFragment
import com.example.transcribeapp.history.HistoryDataBase.Companion.getDataBase
import com.example.transcribeapp.history.mvvm.HistoryRepo
import com.example.transcribeapp.history.mvvm.HistoryViewModel
import com.example.transcribeapp.importAllFile.ImportFileRepo
import com.example.transcribeapp.importAllFile.ImportViewModel
import com.example.transcribeapp.recorder.AudioRecorderManager
import com.example.transcribeapp.recorder.SpeechRecognitionManager
import com.example.transcribeapp.summary.SummaryRepo
import com.example.transcribeapp.summary.SummaryViewModel
import org.koin.dsl.module

val allModules = module {
    single { ApiRepository() }
    single { ChatViewModel(repo = get()) }
    single { getDataBase(get()).historyDao() }
    single { HistoryRepo(get()) }
    single { HistoryViewModel(historyDao = get()) }
    single { ImportFileRepo() }
    single { ImportViewModel(repo = get()) }

    // single { HistoryRepo(historyDao = get()) }

    single { SummaryRepo() }
    single { SummaryViewModel(repo = get()) }


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

/*val viewModeModule= module {

}*/

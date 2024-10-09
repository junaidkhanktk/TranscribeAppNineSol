package com.example.transcribeapp.koin

import com.example.transcribeapp.apis.ApiChatService
import com.example.transcribeapp.apis.ApiRepository
import com.example.transcribeapp.apis.ChatViewModel
import com.example.transcribeapp.authorization.dataLogicLayer.AuthRepo
import com.example.transcribeapp.authorization.dataLogicLayer.AuthViewModel
import com.example.transcribeapp.authorization.google.GoogleSignInViewModel
import com.example.transcribeapp.authorization.interfaces.AuthService
import com.example.transcribeapp.calanderEvents.logicLayer.CalenderEventRepo
import com.example.transcribeapp.calanderEvents.logicLayer.CalenderEventViewModel
import com.example.transcribeapp.client.AuthInterceptor
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.databinding.FragmentConversationBinding
import com.example.transcribeapp.fragment.ConversationFragment
import com.example.transcribeapp.helpers.TinyDB
import com.example.transcribeapp.history.HistoryDataBase.Companion.getDataBase
import com.example.transcribeapp.history.mvvm.HistoryRepo
import com.example.transcribeapp.history.mvvm.HistoryViewModel
import com.example.transcribeapp.history.server.aichat.AiChatService
import com.example.transcribeapp.history.server.event.EventApiService
import com.example.transcribeapp.calanderEvents.eventCalender.CalenderEventService
import com.example.transcribeapp.history.server.get.GetRecordingApiService
import com.example.transcribeapp.history.server.logicLayer.UserHistoryRepo
import com.example.transcribeapp.history.server.logicLayer.UserHistoryViewModel
import com.example.transcribeapp.history.server.upload.UploadRecordApiService
import com.example.transcribeapp.importAllFile.ImportApiService
import com.example.transcribeapp.importAllFile.ImportFileRepo
import com.example.transcribeapp.importAllFile.ImportViewModel
import com.example.transcribeapp.recorder.AudioRecorderManager
import com.example.transcribeapp.recorder.SpeechRecognitionManager
import com.example.transcribeapp.summary.SummaryRepo
import com.example.transcribeapp.summary.SummaryViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    single { TinyDB(get()) }
    single { AuthInterceptor(get()) }

}

val networkModule = module {


    /* single {
         Retrofit.Builder()
             .baseUrl(Keys.getAuthUrl())
             .addConverterFactory(GsonConverterFactory.create())
             .client(
                 OkHttpClient.Builder()
                     .connectTimeout(30, TimeUnit.SECONDS)
                     .readTimeout(30, TimeUnit.SECONDS)
                     .writeTimeout(30, TimeUnit.SECONDS)
                     .addInterceptor(GlobalContext.get().get<AuthInterceptor>())
                     .build()
             )
             .build()
     }*/



    single {
        /*instanceCount += 1
        "Retrofit instance created. Current count1: $instanceCount".log(Log.DEBUG,"RetroFitHelper")*/

        Retrofit.Builder()
            .baseUrl(Keys.getAuthUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(GlobalContext.get().get<AuthInterceptor>())
                    .build()
            )
            .build()
    }


    single {
        get<Retrofit>().create(ImportApiService::class.java)
    }

    single {
        get<Retrofit>().create(AuthService::class.java)
    }

    single {
        get<Retrofit>().create(UploadRecordApiService::class.java)
    }

    single {
        get<Retrofit>().create(GetRecordingApiService::class.java)
    }

    single {
        get<Retrofit>().create(EventApiService::class.java)
    }

    single {
        get<Retrofit>().create(CalenderEventService::class.java)
    }

    single {
        get<Retrofit>().create(AiChatService::class.java)
    }


    single {
        get<Retrofit>().create(AuthService::class.java)
    }

    single {
        get<Retrofit>().create(ApiChatService::class.java)
    }


    /*
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

        single {
            RetroFitHelper(
                baseUrl = Keys.getAuthUrl(),
                apiService = UploadCalenderEventService::class.java
            ).service
        }


        single {
            RetroFitHelper(
                baseUrl = Keys.getAuthUrl(),
                apiService = AiChatService::class.java
            ).service
        }


        single {
            RetroFitHelper(
                baseUrl = Keys.getAuthUrl(),
                apiService = AuthService::class.java
            ).service
        }*/


}

val viewModelModule = module {
    single { HistoryRepo(get()) }
    single { HistoryViewModel(historyDao = get()) }
    single { ApiRepository(get()) }
    single { ChatViewModel(repo = get()) }

    single { ImportFileRepo(get()) }
    single { ImportViewModel(repo = get()) }

    // single { HistoryRepo(historyDao = get()) }

    single { SummaryRepo() }
    single { SummaryViewModel(repo = get()) }

    single { AuthRepo(get()) }
    single { AuthViewModel(get()) }

    viewModel { GoogleSignInViewModel(get()) }
    single {
        UserHistoryRepo(
            uploadRService = get(),
            getRService = get(),
            getEventService = get(),
            aiChatService = get()
        )
    }
    single { UserHistoryViewModel(repo = get()) }

    single {
        TinyDB(get())
    }

    single { CalenderEventRepo(calenderEventService = get()) }
    single { CalenderEventViewModel(get()) }

}

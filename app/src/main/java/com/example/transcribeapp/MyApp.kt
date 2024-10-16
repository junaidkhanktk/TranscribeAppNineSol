package com.example.transcribeapp

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.fragment.ConversationFragment
import com.example.transcribeapp.koin.InitKoin
import com.example.transcribeapp.koin.allModules
import com.example.transcribeapp.koin.networkModule
import com.example.transcribeapp.recorder.SpeechModelProvider
import com.example.transcribeapp.recorder.SpeechRecognitionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        InitKoin(this).load()

      /*  startKoin {
            androidContext(this@MyApp)
            modules(listOf(allModules,networkModule))
        }*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        CoroutineScope(Dispatchers.IO).launch {
            SpeechModelProvider.initializeModel(applicationContext)
        }

    }
}
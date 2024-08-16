package com.example.transcribeapp

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.fragment.ConversationFragment
import com.example.transcribeapp.koin.allModules
import com.example.transcribeapp.recorder.SpeechRecognitionManager
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(allModules))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
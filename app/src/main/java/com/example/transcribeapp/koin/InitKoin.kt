package com.example.transcribeapp.koin

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class InitKoin(var context: Application) {

    fun load(){
        startKoin {
            androidContext(context)
            androidLogger()
            loadKoinModules(listOf(allModules, networkModule,viewModelModule))
        }
    }

}
package com.example.transcribeapp.recorder

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.vosk.Model
import org.vosk.android.StorageService
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SpeechModelProvider {
    private var model: Model? = null

    suspend fun initializeModel(context: Context): Model? {
        if (model == null) {
            model = loadModel(context)
        }
        return model
    }

    private suspend fun loadModel(context: Context): Model? {
        return try {
            suspendCoroutine { continuation ->
                StorageService.unpack(context, "model-en-us", "model",
                    { model -> // Success callback
                        continuation.resume(model)
                    },
                    { e -> // Error callback
                        Log.e("SpeechModelProvider", "Failed to load model: ${e.message}")
                        continuation.resumeWithException(e)
                    })
            }
        } catch (e: Exception) {
            Log.e("SpeechModelProvider", "Failed to load model: ${e.message}")
            null
        }
    }

    fun getModel(): Model? = model
}


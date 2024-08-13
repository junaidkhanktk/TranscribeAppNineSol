package com.example.transcribeapp.recorder


import android.content.Context
import android.util.Log
import com.example.transcribeapp.extension.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.StorageService
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class SpeechRecognitionManager(
    private val context: Context,
    private val recognitionListener: RecognitionListener
) : CoroutineScope {

    private var model: Model? = null
    private var speechService: SpeechService? = null
    private val logTag = "SpeechRecognitionManager"

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun initModel(onModelInitialized: () -> Unit) {
        launch(Dispatchers.IO){
        StorageService.unpack(context, "model-en-us", "model",
            { model -> // Success callback
                this@SpeechRecognitionManager.model = model
                launch(Dispatchers.Main) {
                    onModelInitialized()
                }
            },
            { e -> // Error callback
                "Failed to unpack the model: ${e.message}".log(Log.DEBUG,logTag)
                launch(Dispatchers.Main) {
                    recognitionListener.onError(e)
                }
            })
        }
    }

    fun startRecognition() {
        if (speechService != null) {
            stopRecognition()
        } else {
            model?.let {
                launch(Dispatchers.IO) {
                    try {
                        val recognizer = Recognizer(it, 16000.0f)
                        speechService = SpeechService(recognizer, 16000.0f).apply {
                            startListening(recognitionListener)
                        }
                        withContext(Dispatchers.Main) {
                            recognitionListener.onResult("Recognition started")
                        }
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            recognitionListener.onError(e)
                        }
                    }
                }
            } ?: run {
                "Model is not loaded.".log(Log.DEBUG,logTag)
                recognitionListener.onError(Exception("Model is not loaded."))
            }
        }
    }

    private fun stopRecognition() {
        speechService?.let {
            it.stop()
            speechService = null
            recognitionListener.onResult("Recognition stopped")
        }
    }

    fun pauseRecognition(shouldPause: Boolean) {
        speechService?.setPause(shouldPause)
    }

    fun release() {
        stopRecognition()
        speechService?.shutdown()
        job.cancel()
    }
}









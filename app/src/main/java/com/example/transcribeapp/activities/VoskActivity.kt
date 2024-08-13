
package com.example.transcribeapp.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.transcribeapp.R
import com.example.transcribeapp.recorder.AudioRecorderManager
import org.json.JSONObject
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService
import org.vosk.android.StorageService
import java.io.IOException

class VoskActivity : Activity(), RecognitionListener {
    private var model: Model? = null
    private var speechService: SpeechService? = null
    private var speechStreamService: SpeechStreamService? = null
    private var resultView: TextView? = null
    var txtAudio: AppCompatTextView? = null
    var audioRecorderManger: AudioRecorderManager?=null


    @RequiresApi(Build.VERSION_CODES.N)
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
       // audioRecorderManger = AudioRecorderManager(this)

        // Setup layout
        resultView = findViewById(R.id.result_text)
        txtAudio = findViewById(R.id.timerView)

        setUiState(STATE_START)
        findViewById<View>(R.id.recognize_file).setOnClickListener { view: View? -> recognizeFile() }
        findViewById<View>(R.id.recognize_mic).setOnClickListener { view: View? -> recognizeMicrophone() }
        (findViewById<View>(R.id.pause) as ToggleButton).setOnCheckedChangeListener { view: CompoundButton?, isChecked: Boolean ->
            pause(
                isChecked
            )
        }
        LibVosk.setLogLevel(LogLevel.INFO)

        // Check if user has given permission to record audio, init the model after permission is granted
        val permissionCheck =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        } else {
            initModel()
        }
    }

    private fun initModel() {
        StorageService.unpack(this, "model-en-us", "model",
            { model: Model? ->
                this.model = model
                setUiState(STATE_READY)
            }
        ) {
            exception: IOException -> setErrorState("Failed to unpack the model" + exception.message)
            Log.d("bbbb", "initModel: ${exception.message}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                initModel()
            } else {
                finish()
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (speechService != null) {
            speechService!!.stop()
            speechService!!.shutdown()
        }
        if (speechStreamService != null) {
            speechStreamService!!.stop()
        }
    }

    override fun onResult(hypothesis: String) {
      //  resultView!!.append(hypothesis + "\n")
        try {
            val jsonObject = JSONObject(hypothesis)
            val text = jsonObject.getString("text")
            resultView?.append("$text\n")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("JSONParseError", "Failed to parse hypothesis: $hypothesis")
        }
    }
/*
    override fun onFinalResult(hypothesis: String) {
        resultView!!.append(hypothesis + "\n")



        setUiState(STATE_DONE)
        if (speechStreamService != null) {
            speechStreamService = null
        }

        Log.d("showtextvalue", "onFinalResult: $hypothesis")
    }

    override fun onPartialResult(hypothesis: String) {
        resultView!!.append(hypothesis + "\n")
    }*/


    override fun onFinalResult(hypothesis: String) {
        try {
            val jsonObject = JSONObject(hypothesis)
            val text = jsonObject.getString("text")
            resultView?.append("$text\n")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("JSONParseError", "Failed to parse hypothesis: $hypothesis")
        }

        setUiState(STATE_DONE)
        speechStreamService = null

        Log.d("showtextvalue", "onFinalResult: $hypothesis")
    }

    override fun onPartialResult(hypothesis: String) {
        try {
            val jsonObject = JSONObject(hypothesis)
            val text = jsonObject.getString("text")
            resultView?.append("$text\n")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("JSONParseError", "Failed to parse hypothesis: $hypothesis")
        }
    }

    override fun onError(e: Exception) {
        setErrorState(e.message)
    }

    override fun onTimeout() {
        setUiState(STATE_DONE)
    }

    private fun setUiState(state: Int) {
        when (state) {
            STATE_START -> {
                resultView!!.setText(R.string.preparing)
                resultView!!.movementMethod = ScrollingMovementMethod()
                findViewById<View>(R.id.recognize_file).isEnabled = false
                findViewById<View>(R.id.recognize_mic).isEnabled = false
                findViewById<View>(R.id.pause).isEnabled = false
            }

            STATE_READY -> {
                resultView!!.setText(R.string.ready)
                (findViewById<View>(R.id.recognize_mic) as Button).setText(R.string.recognize_microphone)
                findViewById<View>(R.id.recognize_file).isEnabled = true
                findViewById<View>(R.id.recognize_mic).isEnabled = true
                findViewById<View>(R.id.pause).isEnabled = false
            }

            STATE_DONE -> {
                (findViewById<View>(R.id.recognize_file) as Button).setText(R.string.recognize_file)
                (findViewById<View>(R.id.recognize_mic) as Button).setText(R.string.recognize_microphone)
                findViewById<View>(R.id.recognize_file).isEnabled = true
                findViewById<View>(R.id.recognize_mic).isEnabled = true
                findViewById<View>(R.id.pause).isEnabled = false
                (findViewById<View>(R.id.pause) as ToggleButton).isChecked = false
            }

            STATE_FILE -> {
                (findViewById<View>(R.id.recognize_file) as Button).setText(R.string.stop_file)
                resultView!!.text = getString(R.string.starting)
                findViewById<View>(R.id.recognize_mic).isEnabled = false
                findViewById<View>(R.id.recognize_file).isEnabled = true
                findViewById<View>(R.id.pause).isEnabled = false
            }

            STATE_MIC -> {
                (findViewById<View>(R.id.recognize_mic) as Button).setText(R.string.stop_microphone)
                resultView!!.text = getString(R.string.say_something)
                findViewById<View>(R.id.recognize_file).isEnabled = false
                findViewById<View>(R.id.recognize_mic).isEnabled = true
                findViewById<View>(R.id.pause).isEnabled = true
            }

            else -> throw IllegalStateException("Unexpected value: $state")
        }
    }

    private fun setErrorState(message: String?) {

        Log.d("error", "setErrorState:$message ")
        resultView!!.text = message
        (findViewById<View>(R.id.recognize_mic) as Button).setText(R.string.recognize_microphone)
        findViewById<View>(R.id.recognize_file).isEnabled = false
        findViewById<View>(R.id.recognize_mic).isEnabled = false
    }

    private fun recognizeFile() {
        if (speechStreamService != null) {
            setUiState(STATE_DONE)
            speechStreamService!!.stop()
            speechStreamService = null
        } else {
            setUiState(STATE_FILE)
            try {
                val rec = Recognizer(
                    model, 16000f, "[\"one zero zero zero one\", " +
                            "\"oh zero one two three four five six seven eight nine\", \"[unk]\"]"
                )
                val ais = assets.open(
                    "10001-90210-01803.wav"
                )
                if (ais.skip(44) != 44L) throw IOException("File too short")
                speechStreamService = SpeechStreamService(rec, ais, 16000f)
                speechStreamService!!.start(this)
            } catch (e: IOException) {
                setErrorState(e.message)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun recognizeMicrophone() {
        if (speechService != null) {

            setUiState(STATE_DONE)
            audioRecorderManger?.stopRecording()
            speechService!!.stop()

            speechService = null
        } else {
            setUiState(STATE_MIC)
            try {
                val rec = Recognizer(model, 16000.0f)
                speechService = SpeechService(rec, 16000.0f)
                speechService!!.startListening(this)
                when {
                    audioRecorderManger!!.onPause -> audioRecorderManger!!.resumeRecording()
                    audioRecorderManger!!.recording -> audioRecorderManger!!.pauseRecording()
                    else -> audioRecorderManger!!.startRecording()
                }
            } catch (e: IOException) {
                setErrorState(e.message)
            }
        }
    }

    private fun pause(checked: Boolean) {
        if (speechService != null) {
            speechService!!.setPause(checked)
        }
    }

    companion object {
        private const val STATE_START = 0
        private const val STATE_READY = 1
        private const val STATE_DONE = 2
        private const val STATE_FILE = 3
        private const val STATE_MIC = 4

        /* Used to handle permission request */
        const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    }
}

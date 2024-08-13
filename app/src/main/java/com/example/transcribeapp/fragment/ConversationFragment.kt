package com.example.transcribeapp.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.databinding.FragmentConversationBinding
import com.example.transcribeapp.extension.afterTime
import com.example.transcribeapp.extension.getCurrentTimeMillis
import com.example.transcribeapp.extension.getFormattedDate
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.parseJson
import com.example.transcribeapp.history.History
import com.example.transcribeapp.permission.PermissionUtils
import com.example.transcribeapp.permission.micPermission
import com.example.transcribeapp.recorder.AudioRecorderManager
import com.example.transcribeapp.recorder.SpeechRecognitionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.vosk.android.RecognitionListener

class ConversationFragment :
    BaseFragment<FragmentConversationBinding>(FragmentConversationBinding::inflate),
    RecognitionListener {

    private lateinit var speechRecognitionManager: SpeechRecognitionManager
    private lateinit var audioRecorderManger: AudioRecorderManager

    private val logTagConversation = "ConverstionScreen"
    private var fullText = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PermissionUtils.checkPermission(
            context = requireActivity(),
            permissionArray = micPermission,
            permissionListener = object : PermissionUtils.OnPermissionListener {
                override fun onPermissionSuccess() {
                    initSpeechRecognition()
                    audioRecorderManger = AudioRecorderManager(requireContext(), binding!!)
                }
            }
        )

        binding?.apply {
            resultText.movementMethod = ScrollingMovementMethod()
            btnStart.setOnClickListener {
                afterTime(2000) {

                }
                speechRecognitionManager.startRecognition()

                when {
                    audioRecorderManger.onPause -> {
                        audioRecorderManger.resumeRecording()
                        pause(true)
                    }

                    audioRecorderManger.recording -> {
                        audioRecorderManger.pauseRecording()
                        pause(true)
                    }

                    else -> audioRecorderManger.startRecording()
                }
            }

            btnStop.setOnClickListener {
                audioRecorderManger.stopRecording()

                binding?.timer?.text = "00:00"

                lifecycleScope.launch() {
                    val history = History(
                        "First",
                        binding?.resultText?.text?.trim().toString(),
                        getFormattedDate(),
                        getCurrentTimeMillis(),
                        audioRecorderManger.fileName
                    )
                    historyViewModel.insertHistory(history)

                /*    withContext(Dispatchers.Main) {

                    }*/
                }

                findNavController().navigateUp()
                "history inserted successfully".log(Log.DEBUG, logTagConversation)
                "result ${binding?.resultText?.text}".log(Log.DEBUG, logTagConversation)
            }
        }
    }

    private fun initSpeechRecognition() {
        speechRecognitionManager = SpeechRecognitionManager(requireContext(), this)
        speechRecognitionManager.initModel {
            setUiState(STATE_READY)
        }
    }

    private fun pause(checked: Boolean) {
        speechRecognitionManager.pauseRecognition(checked)
    }

    private fun setUiState(state: Int) {
        when (state) {
            STATE_START -> binding?.apply {
                btnStart.isEnabled = false
                btnStop.isEnabled = false
            }
            STATE_READY -> binding?.apply {
                btnStart.isEnabled = true
            }
            STATE_DONE -> binding?.apply {
                btnStart.isEnabled = true
                btnStop.isEnabled = true
            }
            STATE_MIC -> binding?.apply {
                btnStart.isEnabled = true
            }
            else -> throw IllegalStateException("Unexpected value: $state")
        }
    }

    private fun parseJson(jsonString: String): String {
        return try {
            // Check if the string is a valid JSON object
            if (jsonString.trim().startsWith("{")) {
                val jsonObject = JSONObject(jsonString)
                jsonObject.optString("partial", jsonObject.optString("text", ""))
            } else {
                // If not a valid JSON, return the string itself or handle as needed
                "RecognitionStatus: $jsonString".log(Log.DEBUG, logTagConversation)

                ""
            }
        } catch (e: JSONException) {

            "parseJson Failed to parse JSON: ${e.message}".log(Log.DEBUG, logTagConversation)

            // Return an empty string or some default value in case of an error
            ""
        }
    }

    override fun onPartialResult(hypothesis: String?) {
        hypothesis?.let {
           // val text = parseJson(it)
            val text = it.parseJson()
            if (text.isNotEmpty()) {
                "onPartialResult $text".log(Log.DEBUG, logTagConversation)
                val partialText = text
                binding?.apply {
                    resultText.text = "$fullText $partialText"
                    scrollView.post {
                        scrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
            }
        }
    }

    override fun onResult(hypothesis: String?) {
        hypothesis?.let {
            val text = it.parseJson()
            if (text.isNotEmpty()) {
                "onResult $text".log(Log.DEBUG, logTagConversation)
                fullText += "$text\n"
                binding?.apply {
                    resultText.text = fullText
                    scrollView.post {
                        scrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
            }
        }
    }

    override fun onFinalResult(hypothesis: String?) {
        hypothesis?.let {
            val text = it.parseJson()
            if (text.isNotEmpty()) {
                "onFinalResult $text".log(Log.DEBUG, logTagConversation)
                fullText += "$text\n"
                binding?.apply {
                    resultText.text = fullText
                    scrollView.post {
                        scrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
            }
        }

        setUiState(STATE_DONE)
    }

    override fun onError(e: Exception?) {
        setErrorState(e?.message)
    }

    override fun onTimeout() {
        setUiState(STATE_DONE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognitionManager.release()
    }

    private fun setErrorState(message: String?) {
        "setErrorState: $message".log(Log.DEBUG, logTagConversation)
    }

    companion object {
        private const val STATE_START = 0
        private const val STATE_READY = 1
        private const val STATE_DONE = 2
        private const val STATE_MIC = 4
    }
}


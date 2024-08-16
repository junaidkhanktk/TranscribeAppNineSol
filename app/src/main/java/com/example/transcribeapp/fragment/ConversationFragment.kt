package com.example.transcribeapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentConversationBinding
import com.example.transcribeapp.extension.getCurrentTimeMillis
import com.example.transcribeapp.extension.getFormattedDate
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.parseJson
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.history.History
import com.example.transcribeapp.permission.PermissionUtils
import com.example.transcribeapp.permission.micPermission
import com.example.transcribeapp.recorder.AudioRecorderManager
import com.example.transcribeapp.recorder.SpeechRecognitionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.vosk.android.RecognitionListener

class ConversationFragment :
    BaseFragment<FragmentConversationBinding>(FragmentConversationBinding::inflate),
    RecognitionListener {

    //private val speechRecognitionManager: SpeechRecognitionManager by inject { parametersOf(this) }
    private lateinit var speechRecognitionManager: SpeechRecognitionManager
    private val audioRecorderManger: AudioRecorderManager by inject { parametersOf(binding) }

    private val logTagConversation = "ConversationScreen"
    private var fullText = ""
    private var titelTxt: String? = null
    private var userTxt: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiState(STATE_START)

        speechRecognitionManager = SpeechRecognitionManager(requireContext(), this)

        initSpeechRecognition()
        binding?.apply {
            resultText.movementMethod = ScrollingMovementMethod()
            btnStart.setOnClickListener {
                PermissionUtils.checkPermission(
                    context = requireActivity(),
                    permissionArray = micPermission,
                    permissionListener = object : PermissionUtils.OnPermissionListener {
                        override fun onPermissionSuccess() {
                            startConversationAndRecording()
                        }
                    }
                )
            }

            btnStop.setOnClickListener {


                userTxt = binding?.resultText?.text?.trim().toString()
              /*  userTxt = "fgdkvcml;,zl; m,kbvl"
                lifecycleScope.launch(Dispatchers.IO) {

                    summaryViewModel.sendRequest(userTxt!!)

                }

                requireContext().showToast("clicked")
                return@setOnClickListener*/
                audioRecorderManger.stopRecording()



                titelTxt = title.text?.trim().toString()
                binding?.apply {
                    if (titelTxt.isNullOrBlank()) {
                        requireContext().showToast("Please Enter title")
                        return@setOnClickListener
                    }


                    timer.text = getString(R.string.time)
                }





                lifecycleScope.launch(Dispatchers.IO) {
                    val history = History(
                        titelTxt!!,
                        userTxt!!,
                        getFormattedDate(),
                        getCurrentTimeMillis(),
                        audioRecorderManger.fileName
                    )
                    "Attempting to insert history".log(Log.DEBUG, logTagConversation)
                    historyViewModel.insertHistory(history)



                    withContext(Dispatchers.Main) {
                        // findNavController().navigateUp()
                        historyViewModel.setSelectedItemHistory(history)
                        val bundle = Bundle()
                        bundle.putString("destination", "sourceB")
                        findNavController().navigate(R.id.idRecordingFragment, bundle)

                        "history inserted successfully".log(Log.DEBUG, logTagConversation)
                        "result ${binding?.resultText?.text}".log(Log.DEBUG, logTagConversation)
                    }

                }


            }
        }
    }

    private fun startConversationAndRecording() {
        speechRecognitionManager.startRecognition() {
            setUiState(STATE_DONE)
        }
        when {
            audioRecorderManger.onPause -> {
                audioRecorderManger.resumeRecording()
                speechRecognitionManager.pauseRecognition(true)
            }

            audioRecorderManger.recording -> {
                audioRecorderManger.pauseRecording()
                speechRecognitionManager.pauseRecognition(true)
            }

            else -> audioRecorderManger.startRecording()
        }
    }

    private fun initSpeechRecognition() {
        speechRecognitionManager.initModel {
            setUiState(STATE_READY)
        }
    }


    private fun setUiState(state: Int) {
        when (state) {
            STATE_START -> binding?.apply {
                btnStart.isEnabled = false
                btnStop.isEnabled = false
                "STATE_START".log(Log.DEBUG, logTagConversation)
            }

            STATE_READY -> binding?.apply {
                btnStart.isEnabled = true
                "STATE_READY".log(Log.DEBUG, logTagConversation)
            }

            STATE_DONE -> binding?.apply {
                btnStart.isEnabled = true
                btnStop.isEnabled = true
                "STATE_DONE".log(Log.DEBUG, logTagConversation)
            }

            STATE_MIC -> binding?.apply {
                btnStart.isEnabled = true
                "STATE_MIC".log(Log.DEBUG, logTagConversation)
            }

            else -> throw IllegalStateException("Unexpected value: $state")
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onPartialResult(hypothesis: String?) {
        hypothesis?.let {
            val text = it.parseJson()
            if (text.isNotEmpty()) {
                "onPartialResult $text".log(Log.DEBUG, logTagConversation)
                binding?.apply {
                    resultText.text = "$fullText $text"
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


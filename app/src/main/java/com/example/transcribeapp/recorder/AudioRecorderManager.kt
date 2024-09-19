package com.example.transcribeapp.recorder


import android.content.Context
import android.media.MediaRecorder
import android.os.Handler
import android.os.Looper
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentConversationBinding
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException

class AudioRecorderManager(
    private val context: Context,
    private val binding: FragmentConversationBinding,
) : TimeHandler.OnTimerUpdateListener {

    var fileName: String = generateFileName()
    private var recorder: MediaRecorder? = null
    private var timeHandler: TimeHandler? = null
    var recording = false
    var onPause = false
 lateinit var audioFile: File




    private fun animatePlayerView() {
        if (recording && !onPause) {
            val amp = recorder?.maxAmplitude
            if (amp != null) {
                binding.soundLevel.setProgress((amp / 32767.0 * 100).toInt())
            }
            CoroutineScope(Dispatchers.Main).launch {
                delay(60)
                animatePlayerView()
            }


        }
    }

    fun resumeRecording() {
        onPause = false
        recorder?.apply {
            resume()
        }
        binding.btnStart.setImageResource(R.drawable.ic_pause)
        animatePlayerView()
        timeHandler?.start()
    }

    fun pauseRecording() {
        onPause = true
        recorder?.pause()
        binding.btnStart.setImageResource(R.drawable.ic_play)
        timeHandler?.pause()
    }

    fun startRecording() {
        fileName = generateFileName()
        recording = true
        timeHandler = TimeHandler(this)
        timeHandler?.start()


        try {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(fileName)
                prepare()
                start()
                binding.btnStart.setImageResource(R.drawable.ic_pause)
                animatePlayerView()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

    }

    fun stopRecording() {
        recording = false
        onPause = false
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        binding.btnStart.setImageResource(R.drawable.ic_play)

        try {
            timeHandler?.stop()
            audioFile = File(fileName)
            audioFile.parentFile?.let { parent ->
                if (!parent.exists()) {
                    parent.mkdir()
                }
            }

            if (!audioFile.exists()) {
                audioFile.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onTimerUpdate(duration: String) {
        CoroutineScope(Dispatchers.Main).launch {
            if (recording)
                binding.timer.text = duration
        }
    }

    private fun generateFileName(): String {
       // val timestamp = System.currentTimeMillis()
        return "${context.cacheDir.absolutePath}/audioRecord.mp3"
    }
}

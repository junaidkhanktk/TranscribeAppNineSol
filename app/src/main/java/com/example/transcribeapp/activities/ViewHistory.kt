package com.example.transcribeapp.activities

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.transcribeapp.dataClasses.WordTimestamp
import com.example.transcribeapp.databinding.ActivityViewHistoryBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.history.History
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource


class ViewHistory : BaseActivity() {
    private val binding by lazy {
        ActivityViewHistoryBinding.inflate(layoutInflater)
    }

    private lateinit var simpleExoPlayer: ExoPlayer
    private var currentItemIndex = 0
    private var currentAudioPath = ""
    private lateinit var wordTimestamps: List<WordTimestamp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        currentItemIndex = intent.getIntExtra("Position", 0)
     /*   historyViewModel.readHistory.observe(this) { historyList ->
            if (currentItemIndex in historyList.indices) {
                val currentItem = historyList[currentItemIndex]
                currentAudioPath = currentItem.audioPath
                updateUI(currentItem)
                prepareMediaPlayer()
            } else {
                showToast("Invalid item")
            }
        }*/

        initListeners()
    }

    private fun updateUI(currentItem: History) {
        binding.apply {
            orgionalTxt.text = currentItem.text
        }
    }

    private fun createWordTimestamps(text: String, audioDurationMs: Long): List<WordTimestamp> {
        val words = text.split(" ")
        val avgWordDurationMs = audioDurationMs / words.size
        var currentTimeMs: Long = 0

        return words.map { word ->
            val wordTimestamp = WordTimestamp(word, currentTimeMs, currentTimeMs + avgWordDurationMs)
            currentTimeMs += avgWordDurationMs
            wordTimestamp
        }
    }

    private fun prepareMediaPlayer() {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)

        val mediaItem = MediaItem.fromUri(currentAudioPath)
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(mediaItem)

        simpleExoPlayer = ExoPlayer.Builder(this).build()
        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.prepare()

        // Assuming you have a way to get the duration of the audio file in milliseconds
        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    wordTimestamps = createWordTimestamps(binding.orgionalTxt.text.toString(), simpleExoPlayer.duration)
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    startHighlighting()
                } else {
                    stopHighlighting()
                }
            }
        })
    }

    private fun startHighlighting() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                updateHighlight(simpleExoPlayer.currentPosition)
                handler.postDelayed(this, 100) // Update every 100 ms
            }
        })
    }

    private fun stopHighlighting() {
        simpleExoPlayer.playWhenReady = false
    }

    private fun updateHighlight1(currentPositionMs: Long) {
        val text = binding.orgionalTxt.text.toString()
        val spannableString = SpannableString(text)

        // Clear previous highlights
        spannableString.getSpans(0, text.length, ForegroundColorSpan::class.java).forEach {
            spannableString.removeSpan(it)
        }

        // Find the word that corresponds to the current position
        wordTimestamps.find { currentPositionMs in it.startTimeMs..it.endTimeMs }?.let { wordTimestamp ->
            val startIndex = text.indexOf(wordTimestamp.word)
            if (startIndex != -1) {
                spannableString.setSpan(
                    ForegroundColorSpan(Color.YELLOW),
                    startIndex,
                    startIndex + wordTimestamp.word.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        binding.orgionalTxt.text = spannableString
    }

    private fun updateHighlight(currentPositionMs: Long) {
        val text = binding.orgionalTxt.text.toString()
        val spannableString = SpannableString(text)

        // Clear previous highlights
        spannableString.getSpans(0, text.length, ForegroundColorSpan::class.java).forEach {
            spannableString.removeSpan(it)
        }

        // Track the current position in the text
        var currentIndex = 0

        // Find the word that corresponds to the current position
        wordTimestamps.find { currentPositionMs in it.startTimeMs..it.endTimeMs }?.let { wordTimestamp ->
            // Find the occurrence of the word that matches the timing
            while (currentIndex < text.length) {
                val startIndex = text.indexOf(wordTimestamp.word, currentIndex)
                if (startIndex == -1) break // Word not found

                // Check if the found word matches the current timing
                if (currentIndex <= startIndex && startIndex + wordTimestamp.word.length <= text.length) {
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.YELLOW),
                        startIndex,
                        startIndex + wordTimestamp.word.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    currentIndex = startIndex + wordTimestamp.word.length
                    break
                } else {
                    currentIndex = startIndex + 1
                }
            }
        }

        binding.orgionalTxt.text = spannableString
    }

    private fun initListeners() {
        binding.btnStart.setOnClickListener {
            try {
                simpleExoPlayer.playWhenReady = true
            } catch (e: Exception) {
                "Exception: ${e.message}".log()
            }
        }

        binding.btnStop.setOnClickListener {
            simpleExoPlayer.playWhenReady = false
        }
    }

    override fun onDestroy() {
        simpleExoPlayer.playWhenReady = false
        super.onDestroy()
    }
}













/*
class ViewHistory : BaseActivity() {
    private val binding by lazy {
        ActivityViewHistoryBinding.inflate(layoutInflater)
    }

    private lateinit var simpleExoPlayer: ExoPlayer

    private var currentItemIndex = 0

    private var currentAudioPath=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        currentItemIndex = intent.getIntExtra("Position", 0)
        historyViewModel.readHistory.observe(this) { historyList ->
            if (currentItemIndex in historyList.indices) {
                val currentItem = historyList[currentItemIndex]
                currentAudioPath=currentItem.audioPath
                "currntpath: ${currentItem.audioPath}".log()
                updateUI(currentItem)
                prepareMediaPlayer()

            } else {
                showToast("invalid item ")
            }

        }

        initListeners()
    }


    private fun updateUI(currentItem: History) {
        binding.apply {
            orgionalTxt.text = currentItem.text

        }

    }


    private fun prepareMediaPlayer() {

        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)
        "currntpath1: ${currentAudioPath}".log()
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(currentAudioPath))

        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        simpleExoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    private fun initListeners() {
        binding.btnStart.setOnClickListener {
            try {
                simpleExoPlayer.playWhenReady = true
            }catch (e:Exception){
                "Exception: ${e.message}".log()
            }

        }

        binding.btnStop.setOnClickListener {
            simpleExoPlayer.playWhenReady = false
        }
    }

    override fun onDestroy() {
        simpleExoPlayer.playWhenReady = false
        super.onDestroy()
    }
}*/



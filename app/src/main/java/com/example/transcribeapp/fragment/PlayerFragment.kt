package com.example.transcribeapp.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.example.transcribeapp.R
import com.example.transcribeapp.dataClasses.WordTimestamp
import com.example.transcribeapp.databinding.FragmentAIChatBinding
import com.example.transcribeapp.databinding.FragmentPlayerBinding
import com.example.transcribeapp.extension.formatMinuteSecond
import com.example.transcribeapp.extension.getFormattedTime
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.history.History
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlinx.coroutines.launch


class PlayerFragment : BaseFragment<FragmentPlayerBinding>(FragmentPlayerBinding::inflate) {
    private val logPlayer = "PlayerScreen"
    private lateinit var simpleExoPlayer: ExoPlayer

    private var currentAudioPath = ""
    private lateinit var wordTimestamps: List<WordTimestamp>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            historyViewModel.selectedItemHistory.collect { historyItem ->

                historyItem?.let {
                    currentAudioPath = historyItem.audioPath
                    binding?.apply {
                        idTxt.text = historyItem.text
                        title.text = historyItem.title
                        dateTime.text =
                            "${historyItem.currentDate}, ${getFormattedTime(historyItem.currentTime)}"
                    }
                }

            }
        }

        prepareMediaPlayer()
        initListeners()

    }


    private fun createWordTimestamps(text: String, audioDurationMs: Long): List<WordTimestamp> {
        val words = text.split(" ")
        val avgWordDurationMs = audioDurationMs / words.size
        var currentTimeMs: Long = 0

        return words.map { word ->
            val wordTimestamp =
                WordTimestamp(word, currentTimeMs, currentTimeMs + avgWordDurationMs)
            currentTimeMs += avgWordDurationMs
            wordTimestamp
        }
    }

    private fun prepareMediaPlayer1() {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(requireContext())

        val mediaItem = MediaItem.fromUri(currentAudioPath)
        val mediaSource =
            ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(mediaItem)

        simpleExoPlayer = ExoPlayer.Builder(requireContext()).build()
        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.prepare()

        // Assuming you have a way to get the duration of the audio file in milliseconds
        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    wordTimestamps = createWordTimestamps(
                        binding?.idTxt?.text.toString(),
                        simpleExoPlayer.duration
                    )
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

    private fun prepareMediaPlayer() {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(requireContext())

        val mediaItem = MediaItem.fromUri(currentAudioPath)
        val mediaSource =
            ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(mediaItem)

        simpleExoPlayer = ExoPlayer.Builder(requireContext()).build()
        simpleExoPlayer.addMediaSource(mediaSource)
        simpleExoPlayer.prepare()

        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                binding?.apply {
                    if (playbackState == Player.STATE_READY) {
                        wordTimestamps = createWordTimestamps(
                            idTxt.text.toString(),
                            simpleExoPlayer.duration
                        )
                        seekBar.max = simpleExoPlayer.duration.toInt()
                        durationTxt.text = formatMinuteSecond(simpleExoPlayer.duration)
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    startHighlighting()
                    binding?.PlayPauseBtn?.setImageResource(R.drawable.ic_pause)
                    updateSeekBar()
                } else {
                    stopHighlighting()
                    binding?.PlayPauseBtn?.setImageResource(R.drawable.ic_play)
                }
            }
        })
    }

    private fun updateSeekBar() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                binding?.seekBar?.progress = simpleExoPlayer.currentPosition.toInt()
                handler.postDelayed(this, 1000) // Update SeekBar every second
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


    private fun updateHighlight(currentPositionMs: Long) {
        val text = binding?.idTxt?.text.toString()
        val spannableString = SpannableString(text)

        // Clear previous highlights
        spannableString.getSpans(0, text.length, ForegroundColorSpan::class.java).forEach {
            spannableString.removeSpan(it)
        }

        // Track the current position in the text
        var currentIndex = 0

        // Find the word that corresponds to the current position
        wordTimestamps.find { currentPositionMs in it.startTimeMs..it.endTimeMs }
            ?.let { wordTimestamp ->
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

        binding?.idTxt?.text = spannableString
    }


    private fun initListeners() {
        binding?.apply {
            /* PlayPauseBtn.setOnClickListener {
                 try {
                     simpleExoPlayer.playWhenReady = true
                 } catch (e: Exception) {
                     "Exception: ${e.message}".log()
                 }
             }*/

            PlayPauseBtn.setOnClickListener {
                try {
                    if (simpleExoPlayer.isPlaying) {
                        simpleExoPlayer.pause()
                    } else {
                        simpleExoPlayer.play()
                    }
                } catch (e: Exception) {
                    "Exception: ${e.message}".log()
                }
            }


            /*   playNextBtn.setOnClickListener {
                   simpleExoPlayer.playWhenReady = false
               }*/

            skipForwardBtn.setOnClickListener {
                val newPosition = simpleExoPlayer.currentPosition + 15000
                simpleExoPlayer.seekTo(newPosition)
            }

            skipBackwardBtn.setOnClickListener {
                val newPosition = simpleExoPlayer.currentPosition - 15000
                simpleExoPlayer.seekTo(newPosition)
            }


            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    if (fromUser) {
                        simpleExoPlayer.seekTo(progress.toLong())
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        }

    }

    override fun onDestroyView() {
        simpleExoPlayer.playWhenReady = false
        super.onDestroyView()
    }

}
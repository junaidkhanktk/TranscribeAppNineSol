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
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.dataClasses.WordTimestamp
import com.example.transcribeapp.databinding.FragmentAIChatBinding
import com.example.transcribeapp.databinding.FragmentPlayerBinding
import com.example.transcribeapp.extension.beGone
import com.example.transcribeapp.extension.beVisible
import com.example.transcribeapp.extension.convertToDateTime
import com.example.transcribeapp.extension.formatMinuteSecond
import com.example.transcribeapp.extension.getFormattedTime
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.history.History
import com.example.transcribeapp.uiState.UiState
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


        val title1 = arguments?.getString("Title")
        val timeDate = arguments?.getLong("TimeStamp")

        binding?.apply {
            title.text = title1
            dateTime.text = timeDate?.let { convertToDateTime(it) }
        }

        backPress {
            findNavController().navigate(R.id.idHomeFragment)
        }


        "recieData $title1,$timeDate".log()

        viewLifecycleOwner.lifecycleScope.launch {

            userHistoryViewModel.eventDetailResult.collect { uiState ->
                when (uiState) {

                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                        binding?.apply {
                            progress.beVisible()
                            idTxt.text = "Loading..."
                            durationTxt.text = ""
                        }

                    }

                    is UiState.Error -> {
                        requireContext().showToast("error ${uiState.message}")

                    }

                    is UiState.Success -> {
                        binding?.apply {
                            progress.beGone()
                            idTxt.text = uiState.data?.data?.text
                            durationTxt.text = uiState.data?.data?.duration ?: "3:44"
                            uiState.data?.data?.recording?.let { prepareMediaPlayer(it) }
                            //dateTime.text =
                            //  "${historyItem.currentDate}, ${getFormattedTime(historyItem.currentTime)}"
                        }


                    }
                }

            }


            /*
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
            */


        }

        //prepareMediaPlayer()
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


    private fun prepareMediaPlayer(audioUrl: String) {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(requireContext())

        val mediaItem =
            MediaItem.fromUri(audioUrl)
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(mediaItem)
        simpleExoPlayer = ExoPlayer.Builder(requireContext()).build()
        simpleExoPlayer.setMediaSource(mediaSource)
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
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun startHighlighting() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                updateHighlight(simpleExoPlayer.currentPosition)
                handler.postDelayed(this, 100)
            }
        })
    }

    private fun stopHighlighting() {
        simpleExoPlayer.playWhenReady = false
    }


    private fun updateHighlight(currentPositionMs: Long) {
        val text = binding?.idTxt?.text.toString()
        val spannableString = SpannableString(text)

        spannableString.getSpans(0, text.length, ForegroundColorSpan::class.java).forEach {
            spannableString.removeSpan(it)
        }

        var currentIndex = 0

        wordTimestamps.find { currentPositionMs in it.startTimeMs..it.endTimeMs }
            ?.let { wordTimestamp ->
                while (currentIndex < text.length) {
                    val startIndex = text.indexOf(wordTimestamp.word, currentIndex)
                    if (startIndex == -1) break
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
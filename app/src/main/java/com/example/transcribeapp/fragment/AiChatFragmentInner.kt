package com.example.transcribeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentAiChatInnerBinding
import com.example.transcribeapp.extension.beGone
import com.example.transcribeapp.extension.beVisible
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.uiState.UiState
import com.google.android.exoplayer2.util.Log
import kotlinx.coroutines.launch
import org.vosk.LogLevel
import kotlin.math.log

class AiChatFragmentInner :
    BaseFragment<FragmentAiChatInnerBinding>(FragmentAiChatInnerBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {

            userHistoryViewModel.eventDetailResult.collect { uiState ->
                when (uiState) {

                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                        binding?.apply {
                            progress.beVisible()

                        }

                    }

                    is UiState.Error -> {
                        requireContext().showToast("error ${uiState.message}")

                    }

                    is UiState.Success -> {
                        binding?.apply {
                            progress.beGone()

                            val chat = uiState.data?.data?.aiChat
                            if (chat == null) {
                                "AIChat is null intitialize dummy chat".log(
                                    android.util.Log.DEBUG,
                                    "ChatFragment"
                                )
                            } else {
                                "populate Chat in Rcv".log(
                                    android.util.Log.DEBUG,
                                    "ChatFragment"
                                )
                            }

                            "AIChat $chat".log(android.util.Log.DEBUG, "ChatFragment")

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


    }

}
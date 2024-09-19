package com.example.transcribeapp.fragment

import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentSummaryBinding
import com.example.transcribeapp.extension.beGone
import com.example.transcribeapp.extension.beVisible
import com.example.transcribeapp.extension.convertToDateTime
import com.example.transcribeapp.extension.removeBoldMarkers
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.launch


class SummaryFragment : BaseFragment<FragmentSummaryBinding>(FragmentSummaryBinding::inflate) {


    var dummyTxt =
        "This doesn't guarantee that you'll be successful. But not being consistent will guarantee that you " +
                "won't reach success. An easy productivity hack, instead of spending time getting in the mood to work, " +
                "just stop working, confront the work, and people think they need perfect conditions to start, when in reality, starting is the perfect condition.," +
                " summary=The speaker emphasizes that while success isn't guaranteed, lack of consistency will ensure failure." +
                " They suggest a productivity tip: rather than waiting for ideal conditions to work, one should simply " +
                "begin the task at hand. Starting is deemed the best condition for productivity, countering the belief " +
                "that perfect circumstances are required to get started., keywords=success, consistency, failure," +
                " productivity, ideal conditions, begin, task, starting, circumstances, report=**Report on " +
                "Productivity and Consistency in Achieving Success**  **Introduction**\n" +
                " The transcribed conversation emphasizes the importance of consistency in achieving success and how it outweighs the need for perfection. It suggests a productivity hack to improve work efficiency and highlights the misconception that perfect conditions are necessary to start a task.\n" +
                "  \n" +
                "  **Consistency Leads to Success**\n" +
                "  The conversation begins by noting that while consistency does not guarantee success, a lack of consistency does guarantee failure. This highlights the significance of maintaining a steady effort towards goals to increase the likelihood of achieving them.\n" +
                "  \n" +
                "  **Productivity Hack**\n" +
                "  A practical productivity hack is proposed as an alternative to seeking the perfect work environment. Instead of wasting time trying to get into the right mindset, the advice is to stop overthinking and simply start working. This strategy aims to eliminate unnecessary delays and excuses, encouraging immediate action towards tasks.\n" +
                "  \n" +
                "  **Starting is the Perfect Condition**\n" +
                "  The conversation challenges the common belief that ideal circumstances are needed to begin working effectively. It emphasizes that the act of starting itself is the ideal condition for progress. By initiating tasks without waiting for perfect conditions, individuals can optimize productivity and move towards success.\n" +
                "  "

    var isExpanded = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val title1 = arguments?.getString("Title")
        val timeDate = arguments?.getLong("TimeStamp")

        binding?.apply {

            expandTxt.setOnClickListener {

                if (isExpanded) {
                    summaryTxt.maxLines = 3
                    // summaryTxt.ellipsize = TextUtils.TruncateAt.END
                    summaryTxt.movementMethod = null

                    expandTxt.setImageResource(R.drawable.expand_text_view)
                } else {
                    summaryTxt.maxLines = Integer.MAX_VALUE
                    summaryTxt.movementMethod = ScrollingMovementMethod()
                    expandTxt.setImageResource(R.drawable.expand_text_view_off)


                }
                isExpanded = !isExpanded

            }



            title.text = title1
            dateTime.text = timeDate?.let { convertToDateTime(it) }
        }


        /*  lifecycleScope.launch {
              summaryViewModel.summaryResponse.collect {
                  binding?.summaryTxt?.text = it
              }
          }*/


        /*
                viewLifecycleOwner.lifecycleScope.launch {
                    importVieModel.transcribeResponse
                        .filterNotNull()
                        .collect { uiState ->
                            when (uiState) {
                                is UiState.Loading -> {
                                    binding?.summaryTxt?.text = "Loading..."

                                }

                                is UiState.Success -> {
                                    binding?.summaryTxt?.text = uiState.data
                                }

                                is UiState.Error -> {
                                    binding?.summaryTxt?.text = "Error: ${uiState.message}"
                                }

                                is UiState.Idle -> {}
                            }

                        }
                }
        */


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
                        binding?.summaryTxt?.text = uiState.message

                    }

                    is UiState.Success -> {
                        binding?.apply {
                            progress.beGone()


                            val modifiedResponse =
                                uiState.data?.data?.summary?.removeBoldMarkers().toString()
                            summaryTxt.text = modifiedResponse
                            //summaryTxt.text = uiState.data?.data?.summary
                            // summaryTxt.text = dummyTxt


                            keyWordsTxt.text = uiState.data?.data?.keywords
                            // binding?.aichatTxt?.text = uiState.data?.data?.report


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
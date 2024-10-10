package com.example.transcribeapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.bottomSheet.recordingBottomSheet
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.client.Keys.token
import com.example.transcribeapp.databinding.FragmentHomeBinding
import com.example.transcribeapp.extension.beGone
import com.example.transcribeapp.extension.beVisible
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.manageBottomNavOnKeyboardState
import com.example.transcribeapp.recyclerView.historyRcv
import com.example.transcribeapp.uiState.UiState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // userHistoryViewModel.getRecordData(1,10)
        uploadStatus()
        binding?.apply {
            clAiMeetingGuide.setOnClickListener {
                findNavController().navigate(R.id.calenderEventFragment)
            }

            /*  searchBoxContainer.searchEditText.setOnFocusChangeListener { view, b ->
                  val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                  binding?.apply {
                      root.manageBottomNavOnKeyboardState(requireContext(), bottomNav)
                  }
              }
              */

           // tinyDB.putValue("authToken", token)


            imgRecording.setOnClickListener {
                requireActivity().recordingBottomSheet(this@HomeFragment)
                /*     val bundle = Bundle()
                     bundle.putString("destination", "sourceA")
                     findNavController().navigate(R.id.idRecordingFragment, bundle)*/
            }

            "myToken ${Keys.token}".log()

            "HasCode MAin : ${historyViewModel.hashCode()}".log(Log.DEBUG, "ConverstionScreen")

            historyRcv(requireContext(), this) { title, recordId, timeStamp ->
                // userHistoryViewModel.clearEventDetails()
                userHistoryViewModel.getEventDetails("null-event-details",recordId)
                val bundle = Bundle()
                bundle.putString("Title", title)
                bundle.putLong("TimeStamp", timeStamp)

                findNavController().navigate(R.id.idRecordingFragment, bundle)
            }

        }

    }

    override fun onResume() {
        super.onResume()
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        binding?.apply {
            root.manageBottomNavOnKeyboardState(requireContext(), bottomNav)
        }
    }


    private fun uploadStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            userHistoryViewModel.uploadResult.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        binding?.progress?.beVisible()
                    }

                    is UiState.Success -> {
                        binding?.progress?.beGone()
                    }

                    is UiState.Error -> {
                        binding?.progress?.beGone()
                    }

                    else -> {}
                }

            }
        }
    }

}
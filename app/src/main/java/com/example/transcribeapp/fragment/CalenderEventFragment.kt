package com.example.transcribeapp.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.bottomSheet.recordingBottomSheet
import com.example.transcribeapp.databinding.FragmentCalenderEventBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.recyclerView.calenderEventRcv
import com.example.transcribeapp.recyclerView.historyRcvWE
import com.example.transcribeapp.utils.Constants


class CalenderEventFragment :
    BaseFragment<FragmentCalenderEventBinding>(FragmentCalenderEventBinding::inflate) {

    val url2 = "api/events/recordingsData"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calenderEventViewModel.getAllCalenderEvent()
        //userHistoryViewModel.url = "api/events/recordingsData"

        calenderEventRcv(requireContext(), binding!!) { eventId ->
            Constants.eventId = eventId
            requireActivity().recordingBottomSheet(this)
            /*  val bundle = Bundle()
              bundle.putString("destination", "sourceA")
              findNavController().navigate(R.id.idRecordingFragment, bundle)*/
        }

        binding?.apply {

            historyRcvWE(
                context = requireContext(),
                recyclerView = historyRcv,
                progress = progress,
                url = url2
            ) { title, recordId, timeStamp ->
                // userHistoryViewModel.clearEventDetails()
                "RecordingId---->$recordId".log()
                //  userHistoryViewModel.getWithoutEventDetails(null, recordId)
                val bundle = Bundle()
                bundle.putString("Title", title)
                bundle.putLong("TimeStamp", timeStamp)
                findNavController().navigate(R.id.idRecordingFragment, bundle)
            }

        }


    }


}



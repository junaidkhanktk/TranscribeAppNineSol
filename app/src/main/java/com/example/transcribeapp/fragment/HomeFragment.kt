package com.example.transcribeapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.bottomSheet.recordingBottomSheet
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.databinding.FragmentHomeBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.manageBottomNavOnKeyboardState
import com.example.transcribeapp.recyclerView.historyRcv
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // userHistoryViewModel.getRecordData(1,10)

        binding?.apply {
            imgRecording.setOnClickListener {
                requireActivity().recordingBottomSheet(this@HomeFragment)
                /*     val bundle = Bundle()
                     bundle.putString("destination", "sourceA")
                     findNavController().navigate(R.id.idRecordingFragment, bundle)*/
            }

            "myToken ${Keys.token}".log()

            "HasCode MAin : ${historyViewModel.hashCode()}".log(Log.DEBUG, "ConverstionScreen")

            historyRcv(requireContext(), this) {
                val bundle = Bundle()
                bundle.putString("destination", "sourceB")
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

}
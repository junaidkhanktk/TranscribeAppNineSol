package com.example.transcribeapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentHomeBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.recyclerView.historyRcv


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            imgRecording.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("destination", "sourceA")
                findNavController().navigate(R.id.idRecordingFragment, bundle)
            }

            "HasCode MAin : ${historyViewModel.hashCode()}".log(Log.DEBUG, "ConverstionScreen")

            historyRcv(requireContext(), this) {
                val bundle = Bundle()
                bundle.putString("destination", "sourceB")
                findNavController().navigate(R.id.idRecordingFragment, bundle)
            }

        }

    }


}
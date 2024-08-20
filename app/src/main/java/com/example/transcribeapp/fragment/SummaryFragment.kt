package com.example.transcribeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentConversationBinding
import com.example.transcribeapp.databinding.FragmentSummaryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SummaryFragment : BaseFragment<FragmentSummaryBinding>(FragmentSummaryBinding::inflate) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            summaryViewModel.summaryResponse.collect{
             binding?.summaryTxt?.text=it
            }



        }

        lifecycleScope.launch {
            importVieModel.transcribeResponse.collect{
                binding?.summaryTxt?.text=it
            }
        }



    }

}
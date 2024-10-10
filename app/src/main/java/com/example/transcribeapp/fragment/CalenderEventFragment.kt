package com.example.transcribeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentCalenderEventBinding
import com.example.transcribeapp.recyclerView.calenderEventRcv
import kotlinx.coroutines.launch


class CalenderEventFragment :
    BaseFragment<FragmentCalenderEventBinding>(FragmentCalenderEventBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calenderEventViewModel.getAllCalenderEvent()
        calenderEventRcv(requireContext(), binding!!) { title, recordId, timeStamp ->

        }


    }


}
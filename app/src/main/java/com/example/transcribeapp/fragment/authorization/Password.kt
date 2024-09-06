package com.example.transcribeapp.fragment.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentPasswordBinding
import com.example.transcribeapp.fragment.BaseFragment


class Password : BaseFragment<FragmentPasswordBinding>(FragmentPasswordBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            next.setOnClickListener {
                findNavController().navigate(R.id.verifyEmail)
            }
        }
    }
}
package com.example.transcribeapp

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.databinding.FragmentSplashBinding
import com.example.transcribeapp.fragment.BaseFragment


class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPress {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    /*    afterTime(1000) {

        }*/
        findNavController().navigate(R.id.idHomeFragment)
        binding?.run {

        }
    }
}
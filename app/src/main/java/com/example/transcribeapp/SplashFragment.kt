package com.example.transcribeapp

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.databinding.FragmentSplashBinding
import com.example.transcribeapp.extension.afterTime
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
package com.example.transcribeapp.fragment.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.authorization.dataClasses.OtpRequest
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.databinding.FragmentVerifyEmailBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.fragment.BaseFragment
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.launch


class VerifyEmail : BaseFragment<FragmentVerifyEmailBinding>(FragmentVerifyEmailBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {


            next.setOnClickListener {
                val otp = pinview.text.toString()
                val otpRequest = OtpRequest(
                    otp = otp,
                    email = "testingturteel@gmail.com"
                )

                authViewModel.verifyOtp(otpRequest)
            }
        }




        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.otpResponse.collect { uiState ->
                when (uiState) {
                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                        //  binding?.summaryTxt?.text = "Loading..."
                    }

                    is UiState.Error -> {
                        "Error ${uiState.message}".log()
                    }

                    is UiState.Success -> {

                   // Keys.token = uiState.data?.user?.token.toString()
                       val token = uiState.data?.user?.token.toString()
                        tinyDB.putValue("authToken",token)
                        "Token ${uiState.data?.user?.token.toString()}".log()
                        findNavController().navigate(R.id.idHomeFragment)
                    }


                }

            }
        }


    }
}
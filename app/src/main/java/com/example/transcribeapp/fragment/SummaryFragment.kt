package com.example.transcribeapp.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.transcribeapp.authorization.dataClasses.RegistrationRequest
import com.example.transcribeapp.databinding.FragmentSummaryBinding
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class SummaryFragment : BaseFragment<FragmentSummaryBinding>(FragmentSummaryBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            summaryViewModel.summaryResponse.collect {
                binding?.summaryTxt?.text = it
            }


        }

        binding?.register?.setOnClickListener {
           // handleRegistration()
        }





        viewLifecycleOwner.lifecycleScope.launch {
            importVieModel.transcribeResponse
                .filterNotNull()
                .collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding?.summaryTxt?.text = "Loading..."

                        }

                        is UiState.Success -> {
                            binding?.summaryTxt?.text = uiState.data
                        }

                        is UiState.Error -> {
                            binding?.summaryTxt?.text = "Error: ${uiState.message}"
                        }

                        is UiState.Idle -> {}
                    }

                }
        }


/*
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.regResponse.collect { uiState ->
                when (uiState) {
                    is UiState.Idle -> {

                    }

                    is UiState.Loading ->{
                        binding?.summaryTxt?.text = "Loading..."
                    }

                    is UiState.Error -> {
                        binding?.summaryTxt?.text = "Error: ${uiState.message}"
                    }

                    is UiState.Success ->{
                        binding?.summaryTxt?.text = uiState.data.toString()
                    }


                }

            }
        }
*/


    }

    fun handleRegistration() {
        val request = RegistrationRequest(
            firstName = "junaid",
            password = "12345678",
            email = "junaidkhan.09ktk@gmail.com"
        )

        authViewModel.register(request)

    }
}
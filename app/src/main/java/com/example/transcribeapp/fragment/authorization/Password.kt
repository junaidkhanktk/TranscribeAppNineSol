package com.example.transcribeapp.fragment.authorization

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.authorization.dataClasses.RegistrationRequest
import com.example.transcribeapp.authorization.passwordValidation.hidePassword
import com.example.transcribeapp.authorization.passwordValidation.validatePassword
import com.example.transcribeapp.databinding.FragmentPasswordBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.logD
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.fragment.BaseFragment
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.launch


class Password : BaseFragment<FragmentPasswordBinding>(FragmentPasswordBinding::inflate) {

    val bundle = Bundle()
//    private val apiViewModel :

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiResponse()
        binding?.apply {
            val bundleEmailValue = arguments?.getString("email")
            val bundleNameValue = arguments?.getString("name")
            logD("bundleEmailValue $bundleEmailValue bundleNameValue $bundleNameValue")
            passwordEditText.hidePassword()

            next.setOnClickListener {

                passwordEditText.text.toString().validatePassword { validationErrors ->

                    if (validationErrors.isEmpty()) {
                        hitApi()
//                        bundle.putString("email",bundleEmailValue)
//                        bundle.putString("name",bundleNameValue)
//                        bundle.putString("password",password)
//                        findNavController().navigate(R.id.verifyEmail)
                    } else {
                        validationErrors.forEach { error ->
                            requireContext().showToast(error)
                        }
                    }

                }

            }


        }
    }

    private fun hitApi(){
        val bundleEmailValue = arguments?.getString("email")
        val bundleNameValue = arguments?.getString("name")
        if (bundleEmailValue != null && bundleNameValue!=null) {
            val request = RegistrationRequest(
                firstName = bundleNameValue,
                password = binding?.passwordEditText?.text?.trim().toString(),
                email = bundleEmailValue
            )
            authViewModel.register(request)
        }

    }

    private fun apiResponse(){
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.regResponse.collect { uiState ->
                when (uiState) {
                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                        //  binding?.summaryTxt?.text = "Loading..."
                    }

                    is UiState.Error -> {
                        if (uiState.message == "Email already exists. OTP sent to email") {/*   val login = LoginRequest(
                                    email = email,
                                    password = "123456"
                                )
                                authViewModel.login(login)*/

                            findNavController().navigate(R.id.verifyEmail)
                            //authViewModel.regResponse
                        }
                        "Error ${uiState.message}".log()
                    }

                    is UiState.Success -> {
                        "Token ${uiState.data?.user?.token.toString()}".log()
                        findNavController().navigate(R.id.verifyEmail)
                    }


                }

            }
        }
    }
}
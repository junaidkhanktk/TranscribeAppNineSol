package com.example.transcribeapp.fragment.authorization

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.authorization.passwordValidation.hidePassword
import com.example.transcribeapp.authorization.passwordValidation.validatePassword
import com.example.transcribeapp.databinding.FragmentPasswordBinding
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.fragment.BaseFragment


class Password : BaseFragment<FragmentPasswordBinding>(FragmentPasswordBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {

            passwordEditText.hidePassword()

            next.setOnClickListener {

                passwordEditText.text.toString().validatePassword { validationErrors ->

                    if (validationErrors.isEmpty()) {
                        findNavController().navigate(R.id.verifyEmail)
                    } else {
                        validationErrors.forEach { error ->
                            requireContext().showToast(error)
                        }
                    }


                }


            }
        }
    }
}
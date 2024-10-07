package com.example.transcribeapp.fragment.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.authorization.passwordValidation.validateEmail
import com.example.transcribeapp.databinding.FragmentEmailBinding
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.fragment.BaseFragment


class EmailFragment : BaseFragment<FragmentEmailBinding>(FragmentEmailBinding::inflate) {

    val bundle = Bundle()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            next.setOnClickListener {
                val email = emailEditText.text?.trim().toString()
                bundle.putString("email",email)
                findNavController().navigate(R.id.createAccount,bundle)
                return@setOnClickListener
                emailEditText.text.toString().validateEmail { validationResult ->
                    if (validationResult == null) {

                    } else {
                        requireContext().showToast(validationResult)
                    }
                }


            }
        }


    }

}
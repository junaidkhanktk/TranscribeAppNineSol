package com.example.transcribeapp.fragment.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentCreateAccountBinding
import com.example.transcribeapp.fragment.BaseFragment


class CreateAccount :
    BaseFragment<FragmentCreateAccountBinding>(FragmentCreateAccountBinding::inflate) {
    val bundle = Bundle()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundleEmailValue = arguments?.getString("email")
        binding?.run  {
         next.setOnClickListener {
             val firstName = firstEditText.text?.trim().toString()
             val lastName = lastNameEditText.text?.trim().toString()

             val fullName = "$firstName $lastName"
             bundle.putString("email",bundleEmailValue)
             bundle.putString("name",fullName)

             findNavController().navigate(R.id.password,bundle)
         }
        }


    }

}
package com.example.transcribeapp.fragment.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.transcribeapp.R
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.databinding.FragmentUserProfileSettingBinding
import com.example.transcribeapp.fragment.BaseFragment


class UserProfileSetting :
    BaseFragment<FragmentUserProfileSettingBinding>(FragmentUserProfileSettingBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
          /*  Glide.with(requireContext())
                .load(Keys.profile)
                .circleCrop()
                .into(profileImg)*/


            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }


        }


    }

}
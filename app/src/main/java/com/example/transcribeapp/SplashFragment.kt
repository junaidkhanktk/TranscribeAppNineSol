package com.example.transcribeapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.databinding.FragmentSplashBinding
import com.example.transcribeapp.extension.afterTime
import com.example.transcribeapp.fragment.BaseFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backPress {

            try {
                val info = requireContext().packageManager.getPackageInfo(
                    "com.example.transcriber.App", // TODO Change the package name
                    PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                   Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            } catch (e: PackageManager.NameNotFoundException) {

            } catch (e: NoSuchAlgorithmException) {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            afterTime(1000) {
            findNavController().navigate(R.id.idHomeFragment)
            }
        binding?.run {

        }
    }
}
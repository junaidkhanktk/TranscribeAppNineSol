package com.example.transcribeapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import com.example.transcribeapp.databinding.FragmentTestBinding
import com.example.transcribeapp.extension.getSHA1Fingerprint
import com.example.transcribeapp.extension.logD
import com.example.transcribeapp.fragment.BaseFragment
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.quickauth.signin.*
import com.microsoft.quickauth.signin.ClientCreatedListener
import com.microsoft.quickauth.signin.MSQASignInClient
import com.microsoft.quickauth.signin.MSQASignInOptions
import com.microsoft.quickauth.signin.error.MSQAException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class TestFragment : BaseFragment<FragmentTestBinding>(FragmentTestBinding::inflate) {
    private var mSignInClient: MSQASignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*backPress {
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

    }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initClient()


        var signInOptions : MSQASignInOptions = MSQASignInOptions.Builder()
            .setConfigResourceId(R.raw.msal_config)
            .build()
        acquireToken()

      /*  MSQASignInClient.create(requireContext(), signInOptions, object : ClientCreatedListener {
            override fun onCreated(client: MSQASignInClient) {
                // use client
            }

            override fun onError(error: MSQAException) {
                // handle error
            }
        })*/

        MSQASignInClient.create(requireContext(),
            MSQASignInOptions.Builder()
                .setConfigResourceId(R.raw.msal_config)
                .build(),
            object : ClientCreatedListener {
                override fun onCreated(client: MSQASignInClient) {
                    // success: use client
                    logD("mSignInClient Successfull MSQASignInClient ${client}")
                }

                override fun onError(error: MSQAException) {
                    // failure: handle error
                    logD("mSignInClient Successfull MSQASignInClient ${error.message}")
                }
            })


        binding?.run {
            msSignButton.setOnClickListener {
                msSignButton.setSignInCallback(requireActivity(), mSignInClient!!) {
                        accountInfo: MSQAAccountInfo?, error: MSQAException? ->
                    if (accountInfo != null) {
                        // successful sign-in: use account
                        logD("mSignInClient Successfull ${accountInfo.email}")
                    } else {
                        logD("mSignInClient Failure ${error?.message}")
                        // unsuccessful sign-in: handle error
                    }
                }
            }


        }
    }








    private fun acquireToken() {
        val mScopes = arrayOf("User.Read")
        mSignInClient?.acquireToken(
            requireActivity(), mScopes
        ) { tokenResult: MSQATokenResult?, error: MSQAException? ->
            updateTokenResult(
                tokenResult,
                error
            )
        }
    }



    private fun updateTokenResult(tokenResult: MSQATokenResult?, error: Exception?) {
       // binding?.tvTokenResult?.text = tokenResult?.accessToken ?: error?.message
    }
}
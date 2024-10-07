package com.example.transcribeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.transcribeapp.databinding.FragmentTestBinding
import com.example.transcribeapp.extension.logD
import com.example.transcribeapp.fragment.BaseFragment
import com.microsoft.quickauth.signin.ClientCreatedListener
import com.microsoft.quickauth.signin.MSQAAccountInfo
import com.microsoft.quickauth.signin.MSQASignInClient
import com.microsoft.quickauth.signin.MSQASignInOptions
import com.microsoft.quickauth.signin.MSQATokenResult
import com.microsoft.quickauth.signin.error.MSQAException
import com.microsoft.quickauth.signin.*
import com.microsoft.quickauth.signin.error.MSQAUiRequiredException
import com.microsoft.quickauth.signin.logger.LogLevel
import com.microsoft.quickauth.signin.view.MSQASignInButton
import java.lang.Exception

class TestFragment : BaseFragment<FragmentTestBinding>(FragmentTestBinding::inflate) {
    private var mSignInClient: MSQASignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initClient()

        var signInOptions : MSQASignInOptions = MSQASignInOptions.Builder()
            .setConfigResourceId(R.raw.msal_config)
            .build()


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

            button.setOnClickListener {
                mSignInClient?.signIn(requireActivity()){accountInfo, error->
                    if (accountInfo != null) {
                        // success: use account
                        logD("mSignInClient Successfull ${accountInfo.email}")
                    } else {
                        // failure: handle the error
                        logD("mSignInClient Failure ${error?.message}")
                    }
                }
            }

//            msSignButtonSetting.setSignInCallback(activity, client) {
//                    accountInfo: MSQAAccountInfo?, error: MSQAException? ->
//                if (accountInfo != null) {
//                    // successful sign-in: use account
//                } else {
//                    // unsuccessful sign-in: handle error
//                }
//            }
        }
    }

//    private fun initClient() {
//        MSQASignInClient.create(
//            requireContext(),
//            MSQASignInOptions.Builder()
//                .setConfigResourceId(R.raw.msal_config)
//                .setEnableLogcatLog(true)
//                .setLogLevel(LogLevel.VERBOSE)
//                .setExternalLogger { logLevel: Int, message: String? -> }
//                .build(),
//            object : ClientCreatedListener {
//                override fun onCreated(client: MSQASignInClient) {
//                    mSignInClient = client
//                    getCurrentAccount()
//                    mSignInButton.setSignInCallback(
//                        requireContext(),
//                        client
//                    ) { accountInfo: MSQAAccountInfo?, error: MSQAException? ->
//                        uploadSignInfo(
//                            accountInfo,
//                            error
//                        )
//                    }
//                }
//
//                override fun onError(error: MSQAException) {
//                    mUserInfoResult.text = "create sign in client error:${error.message}"
//                }
//            })
//    }
//
//    private fun acquireToken() {
//        mSignInClient?.acquireToken(
//            requireActivity(), mScopes
//        ) { tokenResult: MSQATokenResult?, error: MSQAException? ->
//            updateTokenResult(
//                tokenResult,
//                error
//            )
//        }
//    }
//
//    private fun uploadSignInfo(accountInfo: MSQAAccountInfo?, error: Exception?) {
//        binding?.run {
//        if (accountInfo != null) {
//            mUserPhoto.setImageBitmap(ByteCodeUtil.base64ToBitmap(accountInfo.base64Photo))
//            val userInfo = ("MicrosoftAccountInfo{"
//                    + ", fullName="
//                    + accountInfo.fullName
//                    + ", userName="
//                    + accountInfo.userName
//                    + ", givenName="
//                    + accountInfo.givenName
//                    + ", surname="
//                    + accountInfo.surname
//                    + ", email="
//                    + accountInfo.email
//                    + ", id="
//                    + accountInfo.id
//                    + '}')
//            mUserInfoResult.text = userInfo
//            mTokenResult.text = accountInfo.idToken
//        } else {
//            mUserPhoto.setImageBitmap(null)
//            mTokenResult.text = null
//            mUserInfoResult.text = if (error != null) "login error: ${error.message}" else ""
//        }
//        val signIn = accountInfo != null
//
//        status.text = if (signIn) "signed in" else "signed out"
//        mSignInButton.visibility = if (signIn) View.GONE else View.VISIBLE
//        mSignOutButton.visibility = if (signIn) View.VISIBLE else View.GONE
//        mSignButtonSetting.visibility = if (signIn) View.GONE else View.VISIBLE
//        mFirstSignInContainerView.visibility = if (signIn) View.VISIBLE else View.GONE
//        mSecondSignInContainerView.visibility = if (signIn) View.VISIBLE else View.GONE
//        }
//    }
//
//    private fun getCurrentAccount() {
//        if (mSignInClient == null) return
////        mTokenResult.text = null
////        mUserInfoResult.text = null
////        mUserPhoto.setImageBitmap(null)
//        mSignInClient?.getCurrentAccount { accountInfo: MSQAAccountInfo?, error: MSQAException? ->
//            uploadSignInfo(
//                accountInfo,
//                error
//            )
//        }
//    }
//
//    private fun updateTokenResult(tokenResult: MSQATokenResult?, error: Exception?) {
//        binding?.tvTokenResult?.text = tokenResult?.accessToken ?: error?.message
//    }
}
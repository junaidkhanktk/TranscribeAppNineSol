package com.example.transcribeapp.fragment.authorization

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.credentials.GetCredentialException
import androidx.credentials.GetCredentialResponse
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.credentials.CustomCredential
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.authorization.dataClasses.RegistrationRequest
import com.example.transcribeapp.databinding.FragmentSignUpBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.fragment.BaseFragment
import com.example.transcribeapp.uiState.UiState
import com.example.transcribeapp.utils.Constants
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            val credentialManager = androidx.credentials.CredentialManager.create(requireContext())
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(Constants.GOOGLE_SIGNIN_KEY)
                .setAutoSelectEnabled(false)
                .build()

            val request: androidx.credentials.GetCredentialRequest =
                androidx.credentials.GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

            continueEmail.setOnClickListener {
                findNavController().navigate(R.id.idHomeFragment)
            }
            signUpGoogle.setOnClickListener {

                lifecycleScope.launch {
                    try {
                        val result: GetCredentialResponse = credentialManager.getCredential(
                            context = requireContext(),
                            request = request,
                        )
                        handleSignIn(result)
                    } catch (e: GetCredentialCancellationException) {
                        "GetCredentialCancellationException....${e.message}".log()
                    } catch (e: NoCredentialException) {
                        Toast.makeText(
                            requireContext(),
                            "No credential available",
                            Toast.LENGTH_SHORT
                        ).show()
                        "NoCredentialException....${e.message}".log()
                    } catch (e: GetCredentialException) {
                        "Sign-in failed....${e.message}+$".log()
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                authViewModel.regResponse.collect { uiState ->
                    when (uiState) {
                        is UiState.Idle -> {

                        }

                        is UiState.Loading -> {
                            //  binding?.summaryTxt?.text = "Loading..."
                        }

                        is UiState.Error -> {
                            //binding?.summaryTxt?.text = "Error: ${uiState.message}"
                        }

                        is UiState.Success -> {
                            // binding?.summaryTxt?.text = uiState.data.toString()
                        }


                    }

                }
            }

        }


    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        when (credential) {
            is PublicKeyCredential -> {
                val responseJson = credential.data
                "PublicKeyCredential....${responseJson}+".log()
                // Validate and authenticate the responseJson on your server
            }

            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                "PasswordCredential....${username}+${password}".log()

                // Validate and authenticate the username and password on your server
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        Log.d("TAG", "handleSignIn:........... " + googleIdTokenCredential.idToken)
                        // Validate and authenticate the Google ID token on your server

                        googleIdTokenCredential.profilePictureUri
                        val email = googleIdTokenCredential.id
                        val name = googleIdTokenCredential.displayName

                        if (name != null) {
                            handleRegistration(name, email)
                        }

                        "picture....${googleIdTokenCredential.profilePictureUri}+ id: ${googleIdTokenCredential.id} name: ${googleIdTokenCredential.displayName} ".log()


                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("TAG", "Received an invalid Google ID token response", e)
                    }
                } else {
                    Log.e("TAG", "Unexpected type of credential")
                }
            }

            else -> {
                Log.e("TAG", "Unexpected type of credential")
            }
        }
    }


    private fun handleRegistration(userName: String, userEmail: String) {
        val request = RegistrationRequest(
            firstName = userName,
            password = userEmail,
            email = userEmail
        )

        authViewModel.register(request)
    }


}
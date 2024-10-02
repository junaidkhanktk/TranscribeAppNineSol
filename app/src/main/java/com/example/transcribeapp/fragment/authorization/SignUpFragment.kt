package com.example.transcribeapp.fragment.authorization

import android.content.pm.PackageManager
import android.credentials.GetCredentialException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.transcribeapp.R
import com.example.transcribeapp.authorization.dataClasses.RegistrationRequest
import com.example.transcribeapp.client.Keys
import com.example.transcribeapp.databinding.FragmentSignUpBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.fragment.BaseFragment
import com.example.transcribeapp.uiState.UiState
import com.example.transcribeapp.utils.Constants
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.microsoft.quickauth.signin.ClientCreatedListener
import com.microsoft.quickauth.signin.MSQAAccountInfo
import com.microsoft.quickauth.signin.MSQASignInClient
import com.microsoft.quickauth.signin.MSQASignInOptions
import com.microsoft.quickauth.signin.MSQATokenResult
import com.microsoft.quickauth.signin.error.MSQAException
import cz.msebera.android.httpclient.extras.Base64
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    var email = ""

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

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {

            val credentialManager = androidx.credentials.CredentialManager.create(requireContext())
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false).setServerClientId(Constants.GOOGLE_SIGNIN_KEY)
                .setAutoSelectEnabled(false).build()

            val request: androidx.credentials.GetCredentialRequest = androidx.credentials.GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption).build()

            continueEmail.setOnClickListener {
                findNavController().navigate(R.id.emailFragment)
                // findNavController().navigate(R.id.idHomeFragment)
                //findNavController().navigate(R.id.verifyEmail)

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
                        Toast.makeText(requireContext(),
                            "No credential available try other way",
                            Toast.LENGTH_SHORT).show()
                        "NoCredentialException....${e.message}".log()
                    } catch (e: GetCredentialException) {
                        "Sign-in failed....${e.message}+$".log()
                    }
                }
            }


            viewLifecycleOwner.lifecycleScope.launch {
                authViewModel.loginResponse.collect { uiState ->
                    when (uiState) {
                        is UiState.Idle -> {

                        }

                        is UiState.Loading -> {
                            //  binding?.summaryTxt?.text = "Loading..."
                        }

                        is UiState.Error -> {

                            "Error ${uiState.message}".log()
                        }

                        is UiState.Success -> {
                            Keys.token = uiState.data?.user?.token.toString()
                            "Token ${uiState.data?.user?.token.toString()}".log()
                            findNavController().navigate(R.id.idHomeFragment)
                        }

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

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
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
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

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
        email = userEmail


        /* val login = LoginRequest(
             email = userEmail,
             password = "123456"
         )
         authViewModel.login(login)*/

        val request = RegistrationRequest(firstName = userName,
            password = "123456",
            email = userEmail)

        authViewModel.register(request)

    }





}
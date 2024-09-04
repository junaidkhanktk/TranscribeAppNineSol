package com.example.transcribeapp.authorization.google

import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoogleSignInViewModel(
    private val googleSignInRepository: GoogleSignInRepository
) : ViewModel() {

    private val _googleSignInResult = MutableStateFlow<Result<GetCredentialResponse>?>(null)
    val googleSignInResult: StateFlow<Result<GetCredentialResponse>?> = _googleSignInResult

    fun signInWithGoogle() {
        viewModelScope.launch(Dispatchers.IO) {
            googleSignInRepository.getGoogleCredential()
                .collect { result ->
                    _googleSignInResult.value = result
                }
        }
    }


}

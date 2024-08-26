package com.example.transcribeapp.authorization.dataLogicLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transcribeapp.authorization.dataClasses.OtpRequest
import com.example.transcribeapp.authorization.dataClasses.RegistrationRequest
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepo) : ViewModel() {

    private val _regResponse = MutableStateFlow<UiState<Any>>(UiState.Idle)
    val regResponse: StateFlow<UiState<Any>> get() = _regResponse


    private val _otpResponse = MutableStateFlow<UiState<Any>>(UiState.Idle)
    val otpResponse: StateFlow<UiState<Any>> get() = _otpResponse


    fun register(request: RegistrationRequest) = viewModelScope.launch {
        _regResponse.value = UiState.Loading
        val result = authRepo.register(request)
        if (result.isSuccess) {
            _regResponse.value = UiState.Success(result.getOrNull() ?: "")
        } else {
            _regResponse.value =
                UiState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
        }

    }


    fun verifyOtp(request: OtpRequest) = viewModelScope.launch {
        _otpResponse.value = UiState.Loading
        val result = authRepo.verifyOtp(request)
        if (result.isSuccess) {
            _otpResponse.value = UiState.Success(result.getOrNull() ?: "")
        }else{
            UiState.Error(result.exceptionOrNull()?.message?:"An unknown error occurred")
        }

    }


}


package com.example.transcribeapp.authorization.dataLogicLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.transcribeapp.authorization.dataClasses.LoginRequest
import com.example.transcribeapp.authorization.dataClasses.LoginResponse
import com.example.transcribeapp.authorization.dataClasses.OtpRequest
import com.example.transcribeapp.authorization.dataClasses.OtpResponse
import com.example.transcribeapp.authorization.dataClasses.RegistrationRequest
import com.example.transcribeapp.authorization.dataClasses.RegistrationResponse
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepo) : ViewModel() {

    private val _regResponse = MutableStateFlow<UiState<RegistrationResponse?>>(UiState.Idle)
    val regResponse = _regResponse.asStateFlow()


    private val _otpResponse = MutableStateFlow<UiState<OtpResponse?>>(UiState.Idle)
    val otpResponse = _otpResponse.asStateFlow()

    private val _loginResponse = MutableStateFlow<UiState<LoginResponse?>>(UiState.Idle)
    val loginResponse = _otpResponse.asStateFlow()


    fun register(request: RegistrationRequest) = viewModelScope.launch {
        _regResponse.value = UiState.Loading
        val result = authRepo.register(request)
        if (result.isSuccess) {
            _regResponse.value = UiState.Success(result.getOrNull())
        } else {
            _regResponse.value =
                UiState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
        }
    }


    fun verifyOtp(request: OtpRequest) = viewModelScope.launch {
        _otpResponse.value = UiState.Loading
        val result = authRepo.verifyOtp(request)
        if (result.isSuccess) {
            _otpResponse.value = UiState.Success(result.getOrNull())
        } else {
            UiState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
        }

    }



    fun login(request: LoginRequest) = viewModelScope.launch {
        _loginResponse.value = UiState.Loading
        val result = authRepo.login(request)
        if (result.isSuccess) {
            _loginResponse.value = UiState.Success(result.getOrNull())
        } else {
            UiState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
        }

    }


}


package com.example.transcribeapp.uiState


sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error<out T>(val message: String) : UiState<T>()
    data object Loading : UiState<Nothing>()

}
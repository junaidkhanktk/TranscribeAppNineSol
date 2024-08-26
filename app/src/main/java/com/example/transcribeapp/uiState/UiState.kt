package com.example.transcribeapp.uiState


sealed class UiState<out T> {
  /*  data class Success<out T>(val data: T) : UiState<T>()
    data class Error<out T>(val message: String) : UiState<T>()
    data object Loading : UiState<Nothing>()
    data object Idle : UiState<Nothing>()*/

    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data object Idle : UiState<Nothing>()
}
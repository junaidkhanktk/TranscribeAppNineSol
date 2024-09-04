package com.example.transcribeapp.authorization.google

import android.app.Activity
import androidx.credentials.GetCredentialResponse
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.Flow

interface GoogleSignInRepository {
    suspend fun getGoogleCredential(): Flow<Result<GetCredentialResponse>>
}
package com.example.transcribeapp.authorization.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GoogleSignInRepo (
    private val credentialManager: CredentialManager,
    private val context: Context,
    private val googleIdOption: GetGoogleIdOption
) : GoogleSignInRepository {
    override suspend fun getGoogleCredential(): Flow<Result<GetCredentialResponse>> = flow {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(context, request)
            emit(Result.success(result))
        } catch (e: GetCredentialCancellationException) {
            emit(Result.failure(e))
        } catch (e: NoCredentialException) {
            emit(Result.failure(e))
        } catch (e: GetCredentialException) {
            emit(Result.failure(e))
        }
    }
}
package com.example.transcribeapp.apis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.transcribeapp.apis.ApiRepository


class ChatViewModel(
    private val repo: ApiRepository,
) : ViewModel() {

    private var _chatResponse = MutableLiveData<String>()
    val chatResponse: LiveData<String> = _chatResponse
    private val _chatError = MutableLiveData<String>()
    val chatError:LiveData<String> = _chatError

    fun processChat(request: SimpleChatRequestBody) {
            repo.processChat(
                request,
                onResponse = {
                    _chatResponse.value=it
                },
                onError = {
                    _chatError.value=it
                },
            )


    }

}
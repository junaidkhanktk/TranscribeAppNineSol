package com.example.transcribeapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.transcribeapp.apis.ChatViewModel
import com.example.transcribeapp.databinding.ActivityAiChatBinding

import org.koin.androidx.viewmodel.ext.android.viewModel

class AiChatActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAiChatBinding.inflate(layoutInflater)
    }

    private val chatViewModel: ChatViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        getResponse()
        setContentView(binding.root)
        binding.btnAi.setOnClickListener {
            chatViewModel.processChat(binding.etAi.text.trim().toString())
        }
    }


    private fun getResponse(){
        chatViewModel.chatResponse.observe(this){
            Log.d("TAG", "getResponse: $it")
        }
        chatViewModel.chatResponse.observe(this){
            Log.d("TAG", "getResponse: $it")
        }
    }
}
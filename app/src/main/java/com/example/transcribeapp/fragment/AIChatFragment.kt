package com.example.transcribeapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transcribeapp.R
import com.example.transcribeapp.adapter.ChatMessageAdapter
import com.example.transcribeapp.apis.ChatViewModel
import com.example.transcribeapp.apis.SimpleChatRequestBody
import com.example.transcribeapp.dataClasses.ChatMessage
import com.example.transcribeapp.dataClasses.MessageType
import com.example.transcribeapp.databinding.FragmentAIChatBinding
import com.example.transcribeapp.extension.manageBottomNavOnKeyboardState
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class AIChatFragment : BaseFragment<FragmentAIChatBinding>(FragmentAIChatBinding::inflate) {

    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private val chatViewModel: ChatViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)


        binding?.root?.manageBottomNavOnKeyboardState(requireContext(), bottomNav)


        chatMessageAdapter = ChatMessageAdapter(mutableListOf())
        binding?.rvChatResponses?.adapter = chatMessageAdapter
        binding?.rvChatResponses?.layoutManager = LinearLayoutManager(requireContext())

        getResponse()

        binding?.btnAi?.setOnClickListener {
            val userMessage = binding?.etAi?.text?.trim().toString()
            if (userMessage.isNotEmpty()) {
                chatMessageAdapter.addMessage(
                    ChatMessage(
                        MessageType.QUESTION,
                        userMessage,

                        )
                )
                val chatRequest =SimpleChatRequestBody(
                    chatId = "123_231",
                    prompt =userMessage,
                )


                    chatViewModel.processChat(chatRequest)
                binding?.etAi?.text?.clear()
                binding?.rvChatResponses?.smoothScrollToPosition(chatMessageAdapter.itemCount - 1)
            }
        }
    }

    private fun getResponse() {
        chatViewModel.chatResponse.observe(requireActivity()) { response ->
            Log.d("TAG", "getResponse: $response")
            chatMessageAdapter.addMessage(
                ChatMessage(
                    MessageType.ANSWER,
                    response,

                    )
            )
            binding?.rvChatResponses?.smoothScrollToPosition(chatMessageAdapter.itemCount - 1)
        }
    }
}
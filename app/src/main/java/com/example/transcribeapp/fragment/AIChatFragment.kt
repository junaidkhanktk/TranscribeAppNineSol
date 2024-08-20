package com.example.transcribeapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transcribeapp.R
import com.example.transcribeapp.adapter.ChatMessageAdapter
import com.example.transcribeapp.apis.ChatViewModel
import com.example.transcribeapp.dataClasses.ChatMessage
import com.example.transcribeapp.dataClasses.MessageType
import com.example.transcribeapp.databinding.FragmentAIChatBinding
import com.example.transcribeapp.extension.isKeyboardOpen
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
                        R.drawable.ic_ai_image
                    )
                )
                chatViewModel.processChat(userMessage)
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
                    R.drawable.ic_ai_image
                )
            )
            binding?.rvChatResponses?.smoothScrollToPosition(chatMessageAdapter.itemCount - 1)
        }
    }
}
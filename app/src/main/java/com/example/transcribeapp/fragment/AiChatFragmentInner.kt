package com.example.transcribeapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transcribeapp.R
import com.example.transcribeapp.adapter.ChatMessageAdapter
import com.example.transcribeapp.dataClasses.ChatMessage
import com.example.transcribeapp.dataClasses.MessageType
import com.example.transcribeapp.databinding.FragmentAiChatInnerBinding
import com.example.transcribeapp.extension.beGone
import com.example.transcribeapp.extension.beVisible
import com.example.transcribeapp.extension.generateUniqueChatId
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.showToast
import com.example.transcribeapp.history.server.aichat.AiChatRequestBody
import com.example.transcribeapp.recyclerView.questionsRcv
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.launch

class AiChatFragmentInner :
    BaseFragment<FragmentAiChatInnerBinding>(FragmentAiChatInnerBinding::inflate) {
    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private var recordingId: String? = null
    private var chatId = ""
    private var isInitialLoad = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding?.root?.manageBottomNavOnKeyboardState(requireContext(), bottomNav)


        chatMessageAdapter = ChatMessageAdapter(mutableListOf())

        binding?.apply {
            rvChatResponses.adapter = chatMessageAdapter
            rvChatResponses.layoutManager = LinearLayoutManager(requireContext())



            sendMessage.setOnClickListener {
                val userMessage = binding?.userMsg?.text?.trim().toString()
                if (userMessage.isNotEmpty()) {
                    chatMessageAdapter.addMessage(
                        ChatMessage(
                            MessageType.QUESTION,
                            userMessage,
                        )
                    )

                    recordingId?.let {

                        val chatRequestBody = AiChatRequestBody(
                            chatId = chatId,
                            prompt = userMessage,
                            recordingId = it,
                            eventId = null
                        )

                        userHistoryViewModel.aiChat(chatRequestBody)
                    }

                    userMsg.text?.clear()
                    rvChatResponses.smoothScrollToPosition(chatMessageAdapter.itemCount - 1)
                } else {
                    rcvQuestions.beVisible()
                }
            }


        }





        eventDetail()
        chatResponse()


    }

    private fun eventDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            userHistoryViewModel.eventDetailResult.collect { uiState ->
                when (uiState) {

                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                        binding?.apply {
                            progress.beVisible()
                        }
                    }

                    is UiState.Error -> {
                        requireContext().showToast("Error: ${uiState.message}")  // Show error message
                    }

                    is UiState.Success -> {
                        binding?.apply {
                            progress.beGone()
                            val eventDetails = uiState.data?.data
                            if (eventDetails == null) {
                                "Event details are null in Success state!".log(
                                    Log.DEBUG,
                                    "ChatFragment"
                                )
                                return@collect
                            }

                            val chat = eventDetails.aiChat
                            recordingId = eventDetails.id
                            // recordingId = chat?.recordingId
                            if (chat != null) {
                                chatId = chat.chatId
                            }

                            /*  Here we need to initialize chat
                            if user come first time on this screen
                            for any event with a random chat id,
                            for second time and onward the chat id will
                            be use from server

                                */

                            if (chat?.prompts.isNullOrEmpty()) {

                                // If `chat` is null, Initialize a dummy chat
                                chatId = generateUniqueChatId()
                                recordingId?.let { recId ->
                                    val chatRequestBody = AiChatRequestBody(
                                        chatId = chatId,
                                        prompt = "hello",
                                        recordingId = recId,
                                        eventId = null
                                    )
                                    userHistoryViewModel.aiChat(chatRequestBody)

                                    "AIChat is null, initializing dummy chat.".log(
                                        Log.DEBUG,
                                        "ChatFragment"
                                    )
                                }

                                "Chat prompts are empty or null!".log(Log.DEBUG, "ChatFragment")
                            } else {
                                chat?.prompts?.forEach { prompt ->
                                    chatMessageAdapter.addMessage(
                                        ChatMessage(
                                            MessageType.QUESTION,
                                            prompt.prompt,
                                        )
                                    )
                                    prompt.response.let {
                                        chatMessageAdapter.addMessage(
                                            ChatMessage(
                                                MessageType.ANSWER,
                                                it,

                                                )
                                        )
                                    }
                                }
                            }

                            "AIChat $chat".log(Log.DEBUG, "ChatFragment")
                            "Populated chat in RecyclerView.".log(Log.DEBUG, "ChatFragment")
                        }
                    }
                }
            }
        }

    }

    private fun chatResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            userHistoryViewModel.aiChatResult.collect { uiState ->
                when (uiState) {
                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                        binding?.apply {
                            progress.beVisible()
                        }
                    }

                    is UiState.Error -> {
                        requireContext().showToast("error ${uiState.message}")
                        binding?.apply {
                            progress.beGone()
                        }
                    }

                    is UiState.Success -> {

                        binding?.apply {

                            // recordingId = uiState.data?.chatData?.recordingId

                            val suggestionQ = uiState.data?.chatData?.suggestionQuestion
                            suggestionQ?.let { ques ->
                                questionsRcv(requireContext(), ques, this) {
                                    userMsg.setText(it)
                                    rcvQuestions.beGone()
                                }
                            }



                            "suggestionQ Question $suggestionQ ".log(
                                Log.DEBUG,
                                "ChatFragment"
                            )

                            progress.beGone()


                            val prompts = uiState.data?.chatData?.prompts
                            //val userAnswerServer = chat.prompts

                            if (prompts == null) {
                                "prompts are null in Success state!".log(
                                    Log.DEBUG,
                                    "ChatFragment"
                                )
                                return@collect
                            }

                            prompts.lastOrNull()?.response?.let { latestResponse ->
                                chatMessageAdapter.addMessage(
                                    ChatMessage(
                                        MessageType.ANSWER,
                                        latestResponse,
                                    )
                                )
                            }

                            /*  if (isInitialLoad) {
                                  prompts.forEach { prompt ->
                                      chatMessageAdapter.addMessage(
                                          ChatMessage(
                                              MessageType.ANSWER,
                                              prompt.response,
                                          )
                                      )
                                  }
                                  isInitialLoad = false
                              } else {
                                  prompts.lastOrNull()?.response?.let { latestResponse ->
                                      chatMessageAdapter.addMessage(
                                          ChatMessage(
                                              MessageType.ANSWER,
                                              latestResponse,
                                          )
                                      )
                                  }
                              }*/
                            "prompts are  ${uiState.data.chatData.prompts}".log(
                                Log.DEBUG,
                                "ChatFragment"
                            )
                            binding?.rvChatResponses?.smoothScrollToPosition(chatMessageAdapter.itemCount - 1)


                        }
                    }
                }

            }
        }
    }


}
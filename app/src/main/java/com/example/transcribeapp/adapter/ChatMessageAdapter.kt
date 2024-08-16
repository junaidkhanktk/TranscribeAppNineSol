package com.example.transcribeapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.transcribeapp.R
import com.example.transcribeapp.dataClasses.ChatMessage
import com.example.transcribeapp.dataClasses.MessageType
import com.example.transcribeapp.databinding.ItemAnswerBinding
import com.example.transcribeapp.databinding.ItemQuestionBinding

import kotlin.math.log

class ChatMessageAdapter(private var messages: MutableList<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_QUESTION = 0
        private const val TYPE_ANSWER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].messageType) {
            MessageType.QUESTION -> TYPE_QUESTION
            MessageType.ANSWER -> TYPE_ANSWER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_QUESTION -> {
                val binding = ItemQuestionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false)
                QuestionViewHolder(binding)
            }
            TYPE_ANSWER -> {
                val binding = ItemAnswerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false)
                AnswerViewHolder(binding)
            }

            else -> {

                val binding = ItemQuestionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false)
                QuestionViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_QUESTION -> (holder as QuestionViewHolder).bind(messages[position])
            TYPE_ANSWER -> (holder as AnswerViewHolder).bind(messages[position])
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun setMessages(newMessages: List<ChatMessage>) {
        messages = newMessages.toMutableList()
    }

    fun getLastMessage(): ChatMessage? {
        return if (messages.isNotEmpty()) messages.last() else null
    }

    class QuestionViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.imgQuestion.setImageResource(message.imageResId)
            binding.tvQuestion.text = message.text
        }
    }

    class AnswerViewHolder(private val binding: ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.imgAnswer.setImageResource(message.imageResId)
            binding.tvAnswer.text = message.text
        }
    }
}






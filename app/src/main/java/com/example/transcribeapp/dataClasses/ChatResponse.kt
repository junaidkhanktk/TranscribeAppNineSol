package com.example.transcribeapp.dataClasses

/**
 * @Author: Naveed Ur Rehman
 * @Designation: Android Developer
 * @Date: 07/08/2024
 * @Gmail: naveedurrehman.ninesol@gmail.com
 * @Company: Ninesol Technologies
 */
enum class MessageType {
    QUESTION,
    ANSWER
}

data class ChatMessage(
    val messageType: MessageType,
    val text: String?,
    //val imageResId: Int
)

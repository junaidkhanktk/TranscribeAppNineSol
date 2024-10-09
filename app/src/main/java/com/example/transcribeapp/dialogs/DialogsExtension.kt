package com.example.transcribeapp.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.transcribeapp.databinding.ChatDialogLayoutBinding

fun FragmentActivity.chatDialog() {
    val binding by lazy {
        ChatDialogLayoutBinding.inflate(layoutInflater)
    }
    val chatDialog = Dialog(this)
    chatDialog.setContentView(binding.root)
    chatDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    chatDialog.show()
}
package com.example.transcribeapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.transcribeapp.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

       // historyRcv(this, binding)

    }
}
package com.example.transcribeapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    var homeBinding:ActivityHomeBinding?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }
}
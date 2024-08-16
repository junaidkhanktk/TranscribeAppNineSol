package com.example.transcribeapp.client

object Keys {
    init {
        System.loadLibrary("native-lib")

    }

    external fun getUlrChat(): String
    external fun getSummaryUrl():String


}
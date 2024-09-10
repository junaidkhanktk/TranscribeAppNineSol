package com.example.transcribeapp.client

object Keys {
    init {
        System.loadLibrary("native-lib")

    }

    external fun getUlrChat(): String
    external fun getSummaryUrl(): String
    external fun getAuthUrl(): String
    var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2ZGJmZDhlYzFlZmRkYzc5NmM3NDdjNiIsImlhdCI6MTcyNTcxMzUzMiwiZXhwIjoxNzI2MzE4MzMyfQ.OHWvYbiuhzKh6oNNmEvvN90P0LkhL8ClJs4tWfcfpY8"


}
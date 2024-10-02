package com.example.transcribeapp.client

object Keys {
    init {
        System.loadLibrary("native-lib")

    }



    external fun getUlrChat(): String
    external fun getSummaryUrl(): String
    external fun getAuthUrl(): String
    var token =  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2ZmUyNGIzMGM5YWRiMjEyNGQ0ZWUyOSIsImlhdCI6MTcyNzkzMTU3MiwiZXhwIjoxNzI4NTM2MzcyfQ.kQ_MyvOeSUvc8CvR3C0ITRars-kDkkKBe5k5kMYlQyw"

    val profile = "https://dummyimage.com/100x100/6699cc/000"

}
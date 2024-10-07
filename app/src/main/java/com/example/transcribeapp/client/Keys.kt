package com.example.transcribeapp.client

object Keys {
    init {
        System.loadLibrary("native-lib")

    }

    external fun getUlrChat(): String
    external fun getSummaryUrl(): String
    external fun getAuthUrl(): String
    var token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2ZjQwMWVkZGVhZWI0YmRmNDM0OTRjNSIsImlhdCI6MTcyNzI2NzMwOSwiZXhwIjoxNzI3ODcyMTA5fQ.RWUOKzciBdCjU0m_V6ZT_6USHiB34XpXolxtdjLotp0"
    val profile = "https://dummyimage.com/100x100/6699cc/000"

}
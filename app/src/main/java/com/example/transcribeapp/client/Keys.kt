package com.example.transcribeapp.client

object Keys {
    init {
        System.loadLibrary("native-lib")

    }



    external fun getUlrChat(): String
    external fun getSummaryUrl(): String
    external fun getAuthUrl(): String

    var token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2ZjI5ZTU2Y2YwMGMzMmViMzZjYTNkNSIsImlhdCI6MTcyODU1Njg2NywiZXhwIjoxNzI5MTYxNjY3fQ.mRGi7JTpBIqkF3V_3i725tr6Ny30TPsVy4_8sLSzx3Y"


    val profile = "https://dummyimage.com/100x100/6699cc/000"

}
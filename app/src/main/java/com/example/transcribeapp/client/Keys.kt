package com.example.transcribeapp.client

object Keys {
    init {
        System.loadLibrary("native-lib")

    }



    external fun getUlrChat(): String
    external fun getSummaryUrl(): String
    external fun getAuthUrl(): String

    var token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2ZjI5ZTU2Y2YwMGMzMmViMzZjYTNkNSIsImlhdCI6MTcyNzk1NTI2NywiZXhwIjoxNzI4NTYwMDY3fQ.dKDUM0DYwqls8KDt7D21kutZMkk_zvkuZXPyVe_w80U"


    val profile = "https://dummyimage.com/100x100/6699cc/000"

}
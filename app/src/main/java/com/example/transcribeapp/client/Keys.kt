package com.example.transcribeapp.client

object Keys {
    init {
        System.loadLibrary("native-lib")

    }

    external fun getUlrChat(): String
    external fun getSummaryUrl(): String
    external fun getAuthUrl(): String
    var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2ZWE2YjE1M2Q3MDRjMGRlZGFlNWI5MSIsImlhdCI6MTcyNjYzODg3MCwiZXhwIjoxNzI3MjQzNjcwfQ.Rt9iB98pOriBySLqS5hbBvv5RuimlzboKlU-A87o41c"


}
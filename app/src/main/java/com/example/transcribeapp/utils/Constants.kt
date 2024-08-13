package com.example.transcribeapp.utils

/**
 * @Author: Naveed Ur Rehman
 * @Designation: Android Developer
 * @Date: 29/07/2024
 * @Gmail: naveedurrehman.ninesol@gmail.com
 * @Company: Ninesol Technologies
 */
object Constants {
    const val STATE_START = 0
    const val STATE_READY = 1
    private const val STATE_DONE = 2
    private const val STATE_FILE = 3
    private const val STATE_MIC = 4

    /* Used to handle permission request */
    const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
}
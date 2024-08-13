package com.example.transcribeapp.recorder

import com.example.transcribeapp.extension.formatMinuteSecond
import java.util.Timer
import java.util.TimerTask

class TimeHandler(private var listener: OnTimerUpdateListener) {

    interface OnTimerUpdateListener{
        fun onTimerUpdate(duration: String)
    }

    private var duration : Long = 0
    private var period : Long = 258
    private lateinit var timer : Timer

    fun start(){
        timer = Timer()
        timer.schedule(object : TimerTask(){
            override fun run() {
                duration += period
                listener.onTimerUpdate(formatMinuteSecond(duration))
            }
        },period, period)
    }


    fun pause(){
        timer.cancel()
    }


    fun stop() {
        timer.cancel()
        timer.purge()
    }



}
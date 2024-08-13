package com.example.transcribeapp.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.experimental.and

@SuppressLint("MissingPermission")
class AudioRecordManager(private val context: Context) {


    private fun generateFileName(): String {
        val timestamp = System.currentTimeMillis()
        return "${context.cacheDir.absolutePath}/audioRecord_$timestamp.wav"
    }

    private var recorder: AudioRecord? = null
    private var recordingThread: Thread? = null
    private var isRecording = false
    var fileName: String =  generateFileName()
    private val sampleRate = 8000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    private val bufferElementsToRecord = 1024
    private val bytesPerElement = 2

    init {
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )
    }

    fun startRecording() {
        recorder?.startRecording()
        isRecording = true
        recordingThread = Thread({
            writeAudioDataToFile()
        }, "AudioRecorder Thread")
        recordingThread?.start()
    }

    fun stopRecording() {
        if (recorder != null) {
            isRecording = false
            recorder?.stop()
            recorder?.release()
            recorder = null
            recordingThread = null
        }
    }

    private fun writeAudioDataToFile() {
        val filePath =fileName
        val shortData = ShortArray(bufferElementsToRecord)
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(filePath)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        while (isRecording) {
            recorder?.read(shortData, 0, bufferElementsToRecord)
            try {
                val byteData = shortToByte(shortData)
                outputStream?.write(byteData, 0, bufferElementsToRecord * bytesPerElement)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun shortToByte(shortData: ShortArray): ByteArray {
        val shortArrSize = shortData.size
        val byteArray = ByteArray(shortArrSize * 2)
        for (i in shortData.indices) {
            byteArray[i * 2] = (shortData[i].toInt() and 0x00FF).toByte()
            byteArray[i * 2 + 1] = (shortData[i].toInt() shr 8).toByte()
        }
        return byteArray
    }
}



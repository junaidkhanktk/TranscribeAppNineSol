package com.example.transcribeapp.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.Calendar


fun String.log(logLevel: Int = Log.DEBUG, tag: String = "TESTING") {
    when (logLevel) {
        Log.VERBOSE -> Timber.tag(tag).v(this)
        Log.DEBUG -> Timber.tag(tag).d(this)
        Log.INFO -> Timber.tag(tag).i(this)
        Log.WARN -> Timber.tag(tag).w(this)
        Log.ERROR -> Timber.tag(tag).e(this)
        Log.ASSERT -> Timber.tag(tag).wtf(this)
    }
}


inline fun <reified A : Activity> Context.startNewActivity() {
    this.startActivity(Intent(this, A::class.java))
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun String.indicesOf(word: String): List<Int> {
    val indices = mutableListOf<Int>()
    var index = this.indexOf(word)
    while (index >= 0) {
        indices.add(index)
        index = this.indexOf(word, index + word.length)
    }
    return indices
}
fun afterTime(timeInMillis: Long, listener: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        listener.invoke()
    }, timeInMillis)
}

fun View.beGone() {
    this.visibility = View.GONE
}

fun View.beVisible() {
    this.visibility = View.VISIBLE
}

fun View.beInvisible() {
    this.visibility = View.VISIBLE
}


fun String.parseJson(): String {
    return try {
        // Check if the string is a valid JSON object
        if (this.trim().startsWith("{")) {
            val jsonObject = JSONObject(this)
            jsonObject.optString("partial", jsonObject.optString("text", ""))
        } else {
            // If not a valid JSON, return the string itself or handle as needed
            "RecognitionStatus: $this".log(Log.DEBUG, "parseJson")

            ""
        }
    } catch (e: JSONException) {

        "parseJson Failed to parse JSON: ${e.message}".log(Log.DEBUG, "parseJson")

        // Return an empty string or some default value in case of an error
        ""
    }
}


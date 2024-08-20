package com.example.transcribeapp.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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

fun TextView.setFormattedTextWithDots(text: String, maxLines: Int = 3) {
    // Split the text into lines and limit it to the specified maxLines
    val lines = text.split("\n").take(maxLines)
    // Add a dot at the beginning of each line
    val formattedText = lines.joinToString("\n") { ". $it" }
    // Set the text to the TextView and apply the maxLines property
    this.text = formattedText
    this.maxLines = maxLines
}

fun Context.uriToFile(uri: Uri): File? {
    val inputStream = contentResolver.openInputStream(uri)
    val outputFile = createTempFile("temp", null, cacheDir)

    try {
        if (inputStream != null) {
            val outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(4 * 1024) // Adjust the buffer size as needed
            var read: Int

            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
            return outputFile
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return null
}



fun Window.setLightStatusBar(statusBarColor: Int = Color.WHITE) {
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    this.statusBarColor = statusBarColor

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        @Suppress("DEPRECATION")
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}


fun Context.dpToPx(valueInDp: Float): Float {
    val metrics = this.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}

fun View.isKeyboardOpen(context: Context, isKeyBoardOpen: (Boolean) -> Unit) {
    val listener = ViewTreeObserver.OnGlobalLayoutListener {
        val heightDiff = this@isKeyboardOpen.rootView.height - this@isKeyboardOpen.height
        if (heightDiff > context.dpToPx(200F)) {
            isKeyBoardOpen.invoke(true)
        } else {
            isKeyBoardOpen.invoke(false)
        }
    }

    this.viewTreeObserver.addOnGlobalLayoutListener(listener)

    // Remove the listener when the view is detached
    this.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

        override fun onViewAttachedToWindow(p0: View) {}

        override fun onViewDetachedFromWindow(p0: View) {
            this@isKeyboardOpen.viewTreeObserver.removeOnGlobalLayoutListener(listener)
            this@isKeyboardOpen.removeOnAttachStateChangeListener(this)        }
    })
}

fun View.manageBottomNavOnKeyboardState(context: Context, bottomNav: View) {
    this.isKeyboardOpen(context) { isOpen ->
        if (isOpen) {
            bottomNav.beGone()
        } else {
            bottomNav.beVisible()
        }
    }
}
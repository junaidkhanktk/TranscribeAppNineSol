package com.example.transcribeapp.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

import com.example.transcribeapp.BuildConfig
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.random.Random


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
fun logE(message: String) {
    if (BuildConfig.DEBUG) {
        Log.e("printLog", message)
    }
}

fun logD(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d("printLog", message)
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


fun TextView.setFormattedTextWithDots(text: String?, maxLines: Int = 3) {

    if (text.isNullOrEmpty()) {
        this.text = ".No text available"
        this.maxLines = maxLines
        return
    }

    // Split the text into words, then try to reconstruct it into lines
    val words = text.split(" ")

    val lines = mutableListOf<String>()
    var currentLine = StringBuilder()

    for (word in words) {
        // Check if adding the next word exceeds the max characters in a typical line
        if (currentLine.length + word.length + 1 <= 50) {  // Assume 50 characters per line (customize this as per your TextView width)
            if (currentLine.isNotEmpty()) {
                currentLine.append(" ")
            }
            currentLine.append(word)
        } else {
            // When the current line is full, add it to the lines list
            lines.add(currentLine.toString())
            currentLine = StringBuilder(word)
        }
        // Stop if we have reached the max number of lines
        if (lines.size >= maxLines) break
    }

    // Add the last line if it's not empty
    if (currentLine.isNotEmpty() && lines.size < maxLines) {
        lines.add(currentLine.toString())
    }

    // Add a single  dot at the beginning of each line and three dot at the end
    val formattedText = lines.joinToString("\n") { ". $it ..." }

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


fun String.removeBoldMarkers(): SpannableString {
    val boldPattern = "\\*\\*(.*?)\\*\\*".toRegex()

    val matches = boldPattern.findAll(this)

    // Create a SpannableStringBuilder to build the processed text
    val spannableStringBuilder = SpannableStringBuilder(this)

    // Adjustment variable to account for changes in the length due to removal of "**" markers
    var adjustment = 0

    // Apply bold styling to the identified matches
    matches.forEach { match ->
        val startIndex = match.range.first - adjustment
        val endIndex = match.range.last + 1 - adjustment

        // Apply the bold style to the specified range
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex - 2, // endIndex - 2 to exclude the first "**"
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Remove the "**" markers
        spannableStringBuilder.delete(endIndex - 2, endIndex)
        spannableStringBuilder.delete(startIndex, startIndex + 2)

        // Adjust the offset due to changes in length
        adjustment += 4 // 2 characters removed from both the start and end
    }

    return SpannableString(spannableStringBuilder)
}





fun generateChatId(): String {
    val prefix = "123_"
    val randomDigits = Random.nextInt(1000, 9999)
    return "$prefix$randomDigits"
}

val generatedIds = mutableSetOf<String>() // Global Set to store unique IDs

fun generateUniqueChatId(): String {
    var newId: String
    do {
        val randomDigits = (1000..9999).random() // Generates 4 random digits
        newId = "123_$randomDigits" // ID format: 123_XXXX
    } while (generatedIds.contains(newId)) // Ensure no duplicates

    generatedIds.add(newId) // Add the new unique ID to the set
    return newId
}



fun Fragment.listFragments() {
    val fm = (requireParentFragment() as NavHostFragment).childFragmentManager
    fm.fragments.forEachIndexed { index, fragment ->
      ("Fragment $index: ${fragment.javaClass.simpleName}").log(Log.DEBUG,"FRAGMENTSTACK")
    }
}















fun Context.getSHA1Fingerprint(): String? {
    return try {
        val packageInfo: PackageInfo = this.packageManager.getPackageInfo(
            this.packageName,
            PackageManager.GET_SIGNATURES
        )
        val signatures = packageInfo.signatures
        val md = MessageDigest.getInstance("SHA-1")
        for (signature in signatures) {
            md.update(signature.toByteArray())
            val digest = md.digest()
            val hexString = StringBuilder()
            for (i in digest.indices) {
                if (i > 0) {
                    hexString.append(":")
                }
                val hex = Integer.toHexString(0xFF and digest[i].toInt())
                if (hex.length == 1) {
                    hexString.append("0")
                }
                hexString.append(hex.uppercase())
            }
            return hexString.toString()
        }
        null
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("SHA1", "Package not found", e)
        null
    } catch (e: NoSuchAlgorithmException) {
        Log.e("SHA1", "SHA-1 algorithm not found", e)
        null
    }
}



private fun logMemoryUsage(tag: String = "MemoryUsage") {
    val runtime = Runtime.getRuntime()
    val usedMemory = runtime.totalMemory() - runtime.freeMemory()
    val maxMemory = runtime.maxMemory()
    val availableMemory = runtime.freeMemory()

    Log.d(tag, "Used Memory: ${usedMemory / 1024 / 1024} MB")
    Log.d(tag, "Max Memory: ${maxMemory / 1024 / 1024} MB")
    Log.d(tag, "Free Memory: ${availableMemory / 1024 / 1024} MB")
}

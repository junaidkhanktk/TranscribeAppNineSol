package com.example.transcribeapp.extension

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

fun getFormattedDate(): String {
    val formatter = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
    val date = Date()
    return formatter.format(date)
}


fun convertToDateTime(timestamp: Long): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val instant = Instant.ofEpochSecond(timestamp)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    } else {
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format.format(date)
    }
}


fun getCurrentTimeMillis(): Long {
    val currentTime = System.currentTimeMillis()
    return currentTime
}

// duration minute second
fun formatMinuteSecond(duration: Long): String {
    val seconds = (duration / 1000) % 60
    val minutes = (duration / (1000 * 60)) % 60

    return "%02d:%02d".format(minutes, seconds)
}


// duration hours minute second

fun format(duration: Long): String {
    val milli = duration % 1000
    val seconds = (duration / 1000) % 60
    val minutes = (duration / (1000 * 60) % 60)
    val hours = (duration / (1000 * 60 * 60) % 24)

    return if (hours > 0)
        "%02d:%02d:%02d.%02d".format(hours, minutes, seconds, milli / 10)
    else
        "%02d:%02d.%02d".format(minutes, seconds, milli / 10)
}


fun getFormattedTime(savedTime: Long): String {
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return timeFormatter.format(savedTime)
}


fun getRecordCategory(timestamp: Long): String {
    val recordDate = Date(timestamp * 1000L) // Convert seconds to milliseconds
    val currentDate = Date()

    // Formatters for different comparisons
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    if (dateFormat.format(recordDate) == dateFormat.format(currentDate)) {
        return "Today"
    }
    calendar.add(Calendar.DATE, -1)
    if (dateFormat.format(recordDate) == dateFormat.format(calendar.time)) {
        return "Yesterday"
    }
    calendar.time = currentDate
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    calendar.time = recordDate
    if (calendar.get(Calendar.WEEK_OF_YEAR) == currentWeek) {
        return "This Week"
    }

    calendar.time = currentDate
    val currentMonth = calendar.get(Calendar.MONTH)
    calendar.time = recordDate
    if (calendar.get(Calendar.MONTH) == currentMonth) {
        return "This Month"
    }

    calendar.time = currentDate
    val currentYear = calendar.get(Calendar.YEAR)
    calendar.time = recordDate
    if (calendar.get(Calendar.YEAR) == currentYear) {
        return "This Year"
    }

    return "Older"
}



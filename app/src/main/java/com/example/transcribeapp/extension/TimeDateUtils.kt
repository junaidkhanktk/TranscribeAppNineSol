package com.example.transcribeapp.extension

import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun getFormattedDate(): String {
    val formatter = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
    val date = Date()
    return formatter.format(date)
}

fun getCurrentTimeMillis(): Long {
    val currentTime = System.currentTimeMillis()
    return currentTime
}
 // duration minute second
fun formatMinuteSecond(duration:Long): String {
    val seconds = (duration / 1000) % 60
    val minutes = (duration / (1000 * 60)) % 60

    return "%02d:%02d".format(minutes, seconds)
}



// duration hours minute second

 fun format(duration:Long): String {
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


fun getTimeAgo(savedTime: Long): String {
    val currentTime = System.currentTimeMillis()
    val diff = currentTime - savedTime
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        else -> "just now"
    }
}


fun startUpdatingTimeAgo(savedTime: Long, onUpdate: (String) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    val runnable = object : Runnable {
        override fun run() {
            val timeAgo = getTimeAgo(savedTime)
            onUpdate(timeAgo)
            handler.postDelayed(this, TimeUnit.MINUTES.toMillis(1))
        }
    }
    handler.post(runnable)
}


fun isToday(savedTime: Long): Boolean {
    val currentCalendar = Calendar.getInstance()
    val savedCalendar = Calendar.getInstance().apply { timeInMillis = savedTime }
    return currentCalendar.get(Calendar.YEAR) == savedCalendar.get(Calendar.YEAR) &&
            currentCalendar.get(Calendar.DAY_OF_YEAR) == savedCalendar.get(Calendar.DAY_OF_YEAR)
}

// Method to check if the saved time is yesterday
fun isYesterday(savedTime: Long): Boolean {
    val currentCalendar = Calendar.getInstance()
    val savedCalendar = Calendar.getInstance().apply { timeInMillis = savedTime }
    currentCalendar.add(Calendar.DAY_OF_YEAR, -1)
    return currentCalendar.get(Calendar.YEAR) == savedCalendar.get(Calendar.YEAR) &&
            currentCalendar.get(Calendar.DAY_OF_YEAR) == savedCalendar.get(Calendar.DAY_OF_YEAR)
}

// Method to check if the saved time is this week
fun isThisWeek(savedTime: Long): Boolean {
    val currentCalendar = Calendar.getInstance()
    val savedCalendar = Calendar.getInstance().apply { timeInMillis = savedTime }
    return currentCalendar.get(Calendar.YEAR) == savedCalendar.get(Calendar.YEAR) &&
            currentCalendar.get(Calendar.WEEK_OF_YEAR) == savedCalendar.get(Calendar.WEEK_OF_YEAR)
}

// Method to check if the saved time is this month
fun isThisMonth(savedTime: Long): Boolean {
    val currentCalendar = Calendar.getInstance()
    val savedCalendar = Calendar.getInstance().apply { timeInMillis = savedTime }
    return currentCalendar.get(Calendar.YEAR) == savedCalendar.get(Calendar.YEAR) &&
            currentCalendar.get(Calendar.MONTH) == savedCalendar.get(Calendar.MONTH)
}

// Method to check if the saved time is this year
fun isThisYear(savedTime: Long): Boolean {
    val currentCalendar = Calendar.getInstance()
    val savedCalendar = Calendar.getInstance().apply { timeInMillis = savedTime }
    return currentCalendar.get(Calendar.YEAR) == savedCalendar.get(Calendar.YEAR)
}

// Method to format the date for records saved earlier
fun formatDate(savedTime: Long): String {
    val dateFormatter = SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault())
    val savedDate = Date(savedTime)
    return dateFormatter.format(savedDate)
}

// Method to calculate how much time has passed since the saved time with differentiation
fun getTimeAgoWithDate(savedTime: Long): String {
    return when {
        isToday(savedTime) -> "Today"
        isYesterday(savedTime) -> "Yesterday"
        isThisWeek(savedTime) -> {
            val dateFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
            dateFormatter.format(Date(savedTime))
        }
        isThisMonth(savedTime) -> {
            val dateFormatter = SimpleDateFormat("EEEE d MMMM", Locale.getDefault())
            dateFormatter.format(Date(savedTime))
        }
        isThisYear(savedTime) -> {
            val dateFormatter = SimpleDateFormat("d MMMM", Locale.getDefault())
            dateFormatter.format(Date(savedTime))
        }
        else -> formatDate(savedTime)
    }
}


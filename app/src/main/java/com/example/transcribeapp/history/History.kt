package com.example.transcribeapp.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "history_table",
   // indices = [Index(value = ["title"], unique = true)]
)
data class History(
    val title: String,
    val text: String,
    val currentDate: String,
    val currentTime: Long,
    val audioPath: String,
    ) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}



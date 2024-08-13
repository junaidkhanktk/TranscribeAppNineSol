package com.example.transcribeapp.history

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun readHistory(): Flow<List<History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(history: History)

    @Delete
    fun deleteHistory(history: History)
}


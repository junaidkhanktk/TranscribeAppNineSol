package com.example.transcribeapp.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 4, exportSchema = true)
abstract class HistoryDataBase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
         var INSTANCE: HistoryDataBase? = null

        fun getDataBase(context: Context): HistoryDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            HistoryDataBase::class.java,
            "history_database"
        ).fallbackToDestructiveMigration()
        .build()



    }


}
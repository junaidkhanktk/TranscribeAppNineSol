package com.example.transcribeapp.utils

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class TinyDB(appContext: Context) {
    private val storageFile: File = File(appContext.filesDir, "MyDataStore")
    private val data: MutableMap<String, Any?> = mutableMapOf()

    init {
        load()
    }

    fun <T> putValue(key: String, value: T) {
        data[key] = value
        save()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String, defaultValue: T): T {
        return data[key] as? T ?: defaultValue
    }

    @Suppress("UNCHECKED_CAST")
    private fun load() {
        if (storageFile.exists()) {
            try {
                ObjectInputStream(FileInputStream(storageFile)).use { ois ->
                    data.putAll(ois.readObject() as MutableMap<String, Any>)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun save() {
        try {
            ObjectOutputStream(FileOutputStream(storageFile)).use { oos ->
                oos.writeObject(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
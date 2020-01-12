package com.lukasstancikas.gamesapp.model.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun restoreList(listJson: String?): List<Long>? {
        return listJson?.let {
             Gson().fromJson(
                it,
                object : TypeToken<List<Long>>() {}.type
            )
        }
    }

    @TypeConverter
    fun saveList(listOfLong: List<Long>?): String? {
        return Gson().toJson(listOfLong)
    }
}
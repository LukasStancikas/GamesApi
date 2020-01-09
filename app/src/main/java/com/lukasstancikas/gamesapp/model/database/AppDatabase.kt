package com.lukasstancikas.gamesapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasstancikas.gamesapp.model.Cover
import com.lukasstancikas.gamesapp.model.Game

@Database(entities = arrayOf(Game::class, Cover::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    
    abstract fun coverDao(): CoverDao
}
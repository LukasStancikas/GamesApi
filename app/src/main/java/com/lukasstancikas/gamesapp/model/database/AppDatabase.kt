package com.lukasstancikas.gamesapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lukasstancikas.gamesapp.model.Cover
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import com.lukasstancikas.gamesapp.model.Screenshot

@Database(entities = [Game::class, Cover::class, Keyword::class, Screenshot::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    abstract fun coverDao(): CoverDao

    abstract fun keywordDao(): KeywordDao

    abstract fun screenshotDao(): ScreenshotDao
}
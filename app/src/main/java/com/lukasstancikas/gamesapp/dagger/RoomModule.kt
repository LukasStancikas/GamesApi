package com.lukasstancikas.gamesapp.dagger

import android.app.Application
import androidx.room.Room
import com.lukasstancikas.gamesapp.model.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(
            application,
            AppDatabase::class.java, "game-database"
        ).build()
}
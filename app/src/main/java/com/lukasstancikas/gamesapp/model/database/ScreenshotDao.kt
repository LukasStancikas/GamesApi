package com.lukasstancikas.gamesapp.model.database

import androidx.room.*
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.GameAndCover
import com.lukasstancikas.gamesapp.model.Keyword
import com.lukasstancikas.gamesapp.model.Screenshot
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface ScreenshotDao {
    @Query("SELECT * FROM screenshot WHERE game IN(:gameId)")
    fun getAllFromGame(gameId: Long) : Flowable<List<Screenshot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(games: List<Screenshot>): Single<List<Long>>

    @Delete
    fun delete(games: List<Game>): Completable
}
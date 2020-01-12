package com.lukasstancikas.gamesapp.model.database

import androidx.room.*
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface KeywordDao {
    @Query("SELECT * FROM keyword WHERE game IN(:gameId)")
    fun getAllFromGame(gameId: Long): Flowable<List<Keyword>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(games: List<Keyword>): Single<List<Long>>

    @Delete
    fun delete(games: List<Game>): Completable
}
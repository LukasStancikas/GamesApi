package com.lukasstancikas.gamesapp.model.database

import androidx.room.*
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.GameAndCover
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): Flowable<List<Game>>

    @Transaction
    @Query("SELECT * FROM game")
    fun getAllGamesAndCovers(): Flowable<List<GameAndCover>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(games: List<Game>): Single<List<Long>>

    @Delete
    fun delete(games: List<Game>): Completable
}
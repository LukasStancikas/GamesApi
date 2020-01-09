package com.lukasstancikas.gamesapp.model.database

import androidx.room.*
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.GameAndCover
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): Single<List<Game>>

    @Transaction
    @Query("SELECT * FROM game")
    fun getAllGamesAndCovers(): Single<List<GameAndCover>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(games: List<Game>): Single<List<Long>>

    @Update
    fun updateAll(games: List<Game>): Completable

    @Delete
    fun delete(games: List<Game>): Completable

    @Transaction
    fun upsertAll(objList: List<Game>) {
        val insertResult = insertAll(objList).blockingGet()
        val updateList = mutableListOf<Game>()
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateList.add(objList[i])
            }
        }
        if (updateList.isNotEmpty()) {
            updateAll(updateList).blockingAwait()
        }
    }
}
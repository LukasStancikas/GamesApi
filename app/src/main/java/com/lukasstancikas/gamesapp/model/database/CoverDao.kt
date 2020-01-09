package com.lukasstancikas.gamesapp.model.database

import androidx.room.*
import com.lukasstancikas.gamesapp.model.Cover
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CoverDao {
    @Query("SELECT * FROM cover")
    fun getAll(): Single<List<Cover>>

    @Insert
    fun insertAll(covers: List<Cover>): Single<List<Long>>

    @Update
    fun updateAll(covers: List<Cover>): Completable

    @Delete
    fun delete(covers: List<Cover>): Completable

    @Transaction
    fun upsertAll(objList: List<Cover>) {
        val insertResult = insertAll(objList).blockingGet()
        val updateList = mutableListOf<Cover>()
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
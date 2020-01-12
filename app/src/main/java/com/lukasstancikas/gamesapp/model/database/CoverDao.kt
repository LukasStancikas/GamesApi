package com.lukasstancikas.gamesapp.model.database

import androidx.room.*
import com.lukasstancikas.gamesapp.model.Cover
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CoverDao {
    @Query("SELECT * FROM cover")
    fun getAll(): Single<List<Cover>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(covers: List<Cover>): Single<List<Long>>

    @Delete
    fun delete(covers: List<Cover>): Completable
}
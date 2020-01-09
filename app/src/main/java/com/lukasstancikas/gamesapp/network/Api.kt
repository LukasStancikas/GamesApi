package com.lukasstancikas.gamesapp.network

import com.lukasstancikas.gamesapp.model.Cover
import com.lukasstancikas.gamesapp.model.Game
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("games")
    fun getGames(@Body fieldsBody: RequestBody): Single<List<Game>>

    @POST("covers")
    fun getCovers(@Body fieldsBody: RequestBody): Single<List<Cover>>

    companion object {
        const val BASE_URL = "https://api-v3.igdb.com"
    }
}
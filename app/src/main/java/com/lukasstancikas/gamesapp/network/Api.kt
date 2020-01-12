package com.lukasstancikas.gamesapp.network

import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("games")
    fun getGames(@Body fieldsBody: RequestBody): Single<List<Game>>

    companion object {
        const val BASE_URL = "https://api-v3.igdb.com"
    }
}
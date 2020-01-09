package com.lukasstancikas.gamesapp.network

import android.text.TextUtils
import com.lukasstancikas.gamesapp.model.Cover
import com.lukasstancikas.gamesapp.model.Game
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody

class ApiControllerImpl(
    private val api: Api
) : ApiController {
    override fun getGames(): Single<List<Game>> {
        val fields = "name,rating,cover,summary;"
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), "fields $fields")
        return api.getGames(requestBody)
    }

    override fun getCovers(games: List<Game>): Single<List<Cover>> {
        val gameIds = TextUtils.join(",", games.map(Game::id))
        val bodyString = "fields url,game; where game = ($gameIds);"
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), bodyString)
        return api.getCovers(requestBody)
    }
}
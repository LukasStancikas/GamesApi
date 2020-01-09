package com.lukasstancikas.gamesapp.network

import com.lukasstancikas.gamesapp.model.Game
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody

class ApiControllerImpl(
    private val api: Api
) : ApiController {
    override fun getGames(): Single<List<Game>> {
        val fields = "name,rating,cover.*,summary;"
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), "fields $fields")
        return api.getGames(requestBody)
    }
}
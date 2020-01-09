package com.lukasstancikas.gamesapp.network

import com.lukasstancikas.gamesapp.model.Game
import io.reactivex.Single

interface ApiController {
    fun getGames(): Single<List<Game>>
}
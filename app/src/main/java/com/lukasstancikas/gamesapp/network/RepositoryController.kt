package com.lukasstancikas.gamesapp.network

import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import io.reactivex.Flowable
import io.reactivex.Single

interface RepositoryController {
    fun getGames(): Flowable<List<Game>>

    fun getKeywordsForGame(game: Game): Single<List<Keyword>>
}
package com.lukasstancikas.gamesapp.network

import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import com.lukasstancikas.gamesapp.model.Screenshot
import io.reactivex.Flowable

interface RepositoryController {
    fun getGames(): Flowable<List<Game>>

    fun getKeywordsForGame(game: Game): Flowable<List<Keyword>>

    fun getScreenshotsForGame(game: Game): Flowable<List<Screenshot>>
}
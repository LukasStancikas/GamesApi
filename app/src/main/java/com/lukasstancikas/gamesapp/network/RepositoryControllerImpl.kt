package com.lukasstancikas.gamesapp.network

import android.annotation.SuppressLint
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import com.lukasstancikas.gamesapp.model.database.AppDatabase
import com.lukasstancikas.gamesapp.util.asDriver
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.RequestBody
import timber.log.Timber

class RepositoryControllerImpl(
    private val api: Api,
    private val db: AppDatabase
) : RepositoryController {

    @SuppressLint("CheckResult")
    override fun getGames(): Flowable<List<Game>> {
        val fields = "name,rating,cover.*,summary,keywords;"
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), "fields $fields")

        Singles.zip(
            api.getGames(requestBody),
            getKeywordsFromApi()
        )
            .flatMapCompletable {
                insertToDb(it.first, it.second)
            }
            .asDriver()
            .subscribeBy(
                onError = Timber::e
            )
        return getGamesFromDb()
    }

    override fun getKeywordsForGame(game: Game): Single<List<Keyword>> {
        return game.keywordIds?.let {
            db.keywordDao().getAllFromGame(it)
        } ?: Single.just(emptyList())
    }

    private fun getKeywordsFromApi(): Single<List<Keyword>> {
        val fields = "name;"
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), "fields $fields")
        return api.getKeywords(requestBody)
    }

    private fun insertToDb(games: List<Game>, keywords: List<Keyword>): Completable {
        val insertKeywords =
            db.keywordDao().insertAll(keywords)
        val insertCovers =
            db.coverDao().insertAll(games.map { it.cover }.filterNotNull())
        val insertGames =
            db.gameDao().insertAll(games)

        return insertKeywords
            .concatWith(insertCovers)
            .concatWith(insertGames)
            .ignoreElements()
    }

    private fun getGamesFromDb(): Flowable<List<Game>> {
        return db
            .gameDao()
            .getAllGamesAndCovers()
            .map {
                val gameList = it.map {
                    it.game.copy(cover = it.cover)
                }
                gameList
            }
    }
}
package com.lukasstancikas.gamesapp.network

import android.annotation.SuppressLint
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import com.lukasstancikas.gamesapp.model.Screenshot
import com.lukasstancikas.gamesapp.model.database.AppDatabase
import com.lukasstancikas.gamesapp.util.asDriver
import io.reactivex.Completable
import io.reactivex.Flowable
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
        val fields = "name,rating,cover.*,summary,keywords.*,screenshots.*;"
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), "fields $fields")

        api.getGames(requestBody)
            .flatMapCompletable(::insertToDb)
            .asDriver()
            .subscribeBy(
                onError = Timber::e
            )
        return getGamesFromDb()
    }

    override fun getKeywordsForGame(game: Game): Flowable<List<Keyword>> {
        return db.keywordDao().getAllFromGame(game.id)
    }

    override fun getScreenshotsForGame(game: Game): Flowable<List<Screenshot>> {
        return db.screenshotDao().getAllFromGame(game.id)
    }

    private fun insertToDb(games: List<Game>): Completable {
        val insertKeywords =
            db.keywordDao().insertAll(games.flatMap { game ->
                game.keywords.orEmpty().map { it.copy(game = game.id) }
            })
        val insertScreenshots =
            db.screenshotDao().insertAll(games.flatMap { game -> game.screenshots.orEmpty() })
        val insertCovers =
            db.coverDao().insertAll(games.map { it.cover }.filterNotNull())
        val insertGames =
            db.gameDao().insertAll(games)

        return insertScreenshots
            .concatWith(insertKeywords)
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
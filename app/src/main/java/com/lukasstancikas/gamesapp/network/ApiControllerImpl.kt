package com.lukasstancikas.gamesapp.network

import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.database.AppDatabase
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody

class ApiControllerImpl(
    private val api: Api,
    private val db: AppDatabase
) : ApiController {
    override fun getGames(): Single<List<Game>> {
        val fields = "name,rating,cover.*,summary;"
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), "fields $fields")

        return db
            .gameDao()
            .getAllGamesAndCovers()
            .flatMap {
                if (it.isEmpty()) {
                    api
                        .getGames(requestBody)
                        .flatMap { games ->
                            val insertCovers =
                                Completable.fromCallable {
                                    db.coverDao().upsertAll(games.map { it.cover }.filterNotNull())
                                }
                            val insertGames =
                                Completable.fromCallable { db.gameDao().upsertAll(games) }

                            insertCovers
                                .andThen(insertGames)
                                .andThen(Single.just(games))
                        }
                } else {
                    val gameList = it.map {
                        it.game.copy(cover = it.cover)
                    }
                    Single.just(gameList)
                }
            }
    }
}
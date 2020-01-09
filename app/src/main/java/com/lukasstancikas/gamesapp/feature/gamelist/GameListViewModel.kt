package com.lukasstancikas.gamesapp.feature.gamelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lukasstancikas.gamesapp.model.Cover
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.NetworkRequest
import com.lukasstancikas.gamesapp.network.ApiController
import com.lukasstancikas.gamesapp.util.asDriver
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class GameListViewModel @Inject constructor(private val api: ApiController) : ViewModel() {
    private val _dataStream: MutableLiveData<List<Pair<Game, Cover?>>> = MutableLiveData()
    val dataStream: LiveData<List<Pair<Game, Cover?>>> = _dataStream

    private val _gameLoadStream = MutableLiveData<NetworkRequest>()
    val gameLoadStream: LiveData<NetworkRequest> = _gameLoadStream

    private val compositeDisposable = CompositeDisposable()

    init {
        refreshData()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun refreshData() {
        _gameLoadStream.postValue(NetworkRequest.Loading)
        api
            .getGames()
            .flatMap { games ->
                api
                    .getCovers(games)
                    .flatMap { covers ->
                        pairGamesWithCovers(games, covers)
                    }
            }
            .asDriver()
            .doOnError(Timber::e)
            .doOnSuccess { _gameLoadStream.postValue(NetworkRequest.Done) }
            .subscribeBy(
                onSuccess = { _dataStream.postValue(it) },
                onError = ::onLoadError
            )
            .addTo(compositeDisposable)
    }

    private fun pairGamesWithCovers(
        games: List<Game>,
        covers: List<Cover>
    ): Single<List<Pair<Game, Cover?>>> {
        return Single.fromCallable {
            games.map { game ->
                val possibleCover = covers.find { it.game == game.id }
                Pair(game, possibleCover)
            }
        }
    }

    private fun onLoadError(error: Throwable) {
        _gameLoadStream.postValue(NetworkRequest.Error(error))
    }
}
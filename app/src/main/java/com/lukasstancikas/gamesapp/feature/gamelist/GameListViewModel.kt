package com.lukasstancikas.gamesapp.feature.gamelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.NetworkRequest
import com.lukasstancikas.gamesapp.network.RepositoryController
import com.lukasstancikas.gamesapp.util.asDriver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class GameListViewModel @Inject constructor(private val repository: RepositoryController) :
    ViewModel() {
    private val _dataStream: MutableLiveData<List<Game>> = MutableLiveData()
    val dataStream: LiveData<List<Game>> = _dataStream

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
        repository
            .getGames()
            .asDriver()
            .doOnError(Timber::e)
            .doOnNext { _gameLoadStream.postValue(NetworkRequest.Done) }
            .subscribeBy(
                onNext = { _dataStream.postValue(it) },
                onError = ::onLoadError
            )
            .addTo(compositeDisposable)
    }

    private fun onLoadError(error: Throwable) {
        _gameLoadStream.postValue(NetworkRequest.Error(error))
    }
}
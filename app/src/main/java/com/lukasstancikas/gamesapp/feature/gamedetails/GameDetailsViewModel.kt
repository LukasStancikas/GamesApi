package com.lukasstancikas.gamesapp.feature.gamedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import com.lukasstancikas.gamesapp.model.NetworkRequest
import com.lukasstancikas.gamesapp.network.RepositoryController
import com.lukasstancikas.gamesapp.util.asDriver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class GameDetailsViewModel @Inject constructor(private val repository: RepositoryController) : ViewModel() {
    private val _keywordStream: MutableLiveData<List<Keyword>> = MutableLiveData()
    val keywordStream: LiveData<List<Keyword>> = _keywordStream

    private val _loadStream = MutableLiveData<NetworkRequest>()
    val loadStream: LiveData<NetworkRequest> = _loadStream

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun refreshData(game: Game) {
        _loadStream.postValue(NetworkRequest.Loading)
        repository
            .getKeywordsForGame(game)
            .asDriver()
            .doOnError(Timber::e)
            .doOnSuccess { _loadStream.postValue(NetworkRequest.Done) }
            .subscribeBy(
                onSuccess = { _keywordStream.postValue(it) },
                onError = ::onLoadError
            )
            .addTo(compositeDisposable)
    }

    private fun onLoadError(error: Throwable) {
        _loadStream.postValue(NetworkRequest.Error(error))
    }
}
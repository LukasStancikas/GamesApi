package com.lukasstancikas.gamesapp.feature.gamedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import com.lukasstancikas.gamesapp.model.NetworkRequest
import com.lukasstancikas.gamesapp.model.Screenshot
import com.lukasstancikas.gamesapp.network.RepositoryController
import com.lukasstancikas.gamesapp.util.asDriver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class GameDetailsViewModel @Inject constructor(private val repository: RepositoryController) :
    ViewModel() {
    private val _keywordStream: MutableLiveData<List<Keyword>> = MutableLiveData()
    val keywordStream: LiveData<List<Keyword>> = _keywordStream

    private val _screenshotStream: MutableLiveData<List<Screenshot>> = MutableLiveData()
    val screenshotStream: LiveData<List<Screenshot>> = _screenshotStream

    private val _loadStream = MutableLiveData<NetworkRequest>()
    val loadStream: LiveData<NetworkRequest> = _loadStream

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun refreshData(game: Game) {
        _loadStream.postValue(NetworkRequest.Loading)

        val fetchScreenshots = repository
            .getScreenshotsForGame(game)
        val fetchKeywords = repository
            .getKeywordsForGame(game)

        fetchScreenshots
            .asDriver()
            .doOnError(Timber::e)
            .subscribeBy(
                onNext = { _screenshotStream.postValue(it) },
                onError = Timber::e
            )
            .addTo(compositeDisposable)

        fetchKeywords
            .asDriver()
            .doOnError(Timber::e)
            .subscribeBy(
                onNext = { _keywordStream.postValue(it) },
                onError = Timber::e
            )
            .addTo(compositeDisposable)

        Flowables.zip(fetchKeywords, fetchScreenshots)
            .asDriver()
            .subscribeBy(
                onNext = { _loadStream.postValue(NetworkRequest.Done) },
                onError = { _loadStream.postValue(NetworkRequest.Error(it)) }
            )
            .addTo(compositeDisposable)
    }
}
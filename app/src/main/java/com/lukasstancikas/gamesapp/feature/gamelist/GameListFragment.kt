package com.lukasstancikas.gamesapp.feature.gamelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.lukasstancikas.gamesapp.R
import com.lukasstancikas.gamesapp.dagger.InjectableFragment
import com.lukasstancikas.gamesapp.feature.MainActivity
import com.lukasstancikas.gamesapp.model.Cover
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.NetworkRequest
import com.lukasstancikas.gamesapp.util.injectViewModel
import com.lukasstancikas.gamesapp.util.showShortSnackbar
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_game_list.*
import javax.inject.Inject

class GameListFragment : Fragment(), InjectableFragment, GameAdapter.OnItemClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: GameListViewModel

    private val adapter by lazy {
        GameAdapter().apply {
            setOnItemClickListener(this@GameListFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        return inflater.inflate(R.layout.fragment_game_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameRecycler.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        subscribeToViewModelStreams()
        subscribeViewInteractions()
    }

    override fun onClick(item: Game) {
        (activity as? MainActivity)?.onGameClicked(item)
    }

    @SuppressLint("CheckResult")
    private fun subscribeViewInteractions() {
        gameSwipeRefresh
            .refreshes()
            .bindToLifecycle(this)
            .subscribeBy(
                onNext = { viewModel.refreshData() }
            )
    }

    private fun subscribeToViewModelStreams() {
        viewModel
            .dataStream
            .observe(this, Observer {
                adapter.setItems(it)
            })

        viewModel
            .gameLoadStream
            .observe(this, Observer { gameLoadStatus ->
                when (gameLoadStatus) {
                    NetworkRequest.Loading -> gameSwipeRefresh.isRefreshing = true
                    NetworkRequest.Done -> gameSwipeRefresh.isRefreshing = false
                    is NetworkRequest.Error -> {
                        gameSwipeRefresh.isRefreshing = false
                        showShortSnackbar(gameLoadStatus.throwable.message)
                    }
                }
            })
    }
}
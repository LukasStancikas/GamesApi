package com.lukasstancikas.gamesapp.feature.gamedetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.lukasstancikas.gamesapp.R
import com.lukasstancikas.gamesapp.dagger.InjectableFragment
import com.lukasstancikas.gamesapp.feature.MainActivity
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Keyword
import com.lukasstancikas.gamesapp.model.NetworkRequest
import com.lukasstancikas.gamesapp.model.Screenshot
import com.lukasstancikas.gamesapp.util.argument
import com.lukasstancikas.gamesapp.util.injectViewModel
import com.lukasstancikas.gamesapp.util.showShortSnackbar
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_game_details.*
import javax.inject.Inject

class GameDetailsFragment : Fragment(), InjectableFragment, ScreenshotAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: GameDetailsViewModel

    private val game: Game by argument(KEY_GAME)
    private val adapter by lazy {
        ScreenshotAdapter().apply {
            setOnItemClickListener(this@GameDetailsFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        return inflater.inflate(R.layout.fragment_game_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailsScreenshotsRecycler.adapter = adapter
        gameDetailsToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        showGameDetails()
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData(game)
        subscribeToViewModelStreams()
        subscribeViewInteractions()
    }

    override fun onClick(item: Screenshot) {
        (activity as? MainActivity)?.onScreenshotClicked(item)
    }

    @SuppressLint("CheckResult")
    private fun subscribeViewInteractions() {
        gameDetailsSwipeRefresh
            .refreshes()
            .bindToLifecycle(this)
            .subscribeBy(
                onNext = { viewModel.refreshData(game) }
            )
    }

    private fun showGameDetails() {
        gameDetailsSummary.text = game.summary
        gameDetailsRating.text = game.rating.toString()
        if (game.cover != null) {
            gameDetailsCollapsingLayout.title = game.name
            Glide
                .with(gameDetailsCover.context)
                .load(game.cover?.trimmedUrl())
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(gameDetailsCover)
        } else {
            gameDetailsToolbar.title = game.name
            gameDetailsCollapsingLayout.isTitleEnabled = false
            gameDetailsCover.visibility = View.GONE
        }
    }

    private fun subscribeToViewModelStreams() {
        viewModel
            .keywordStream
            .observe(this, Observer {
                createKeywordChips(it)
            })

        viewModel
            .screenshotStream
            .observe(this, Observer {
                adapter.setItems(it)
            })

        viewModel
            .loadStream
            .observe(this, Observer { loadStatus ->
                when (loadStatus) {
                    NetworkRequest.Loading -> gameDetailsSwipeRefresh.isRefreshing = true
                    NetworkRequest.Done -> gameDetailsSwipeRefresh.isRefreshing = false
                    is NetworkRequest.Error -> {
                        gameDetailsSwipeRefresh.isRefreshing = false
                        showShortSnackbar(loadStatus.throwable.message)
                    }
                }
            })
    }

    private fun createKeywordChips(items: List<Keyword>) {
        gameDetailsKeywords.removeAllViews()
        items.forEach {
            val chip = LayoutInflater.from(gameDetailsKeywords.context)
                .inflate(R.layout.item_keyword, gameDetailsKeywords, false)
            (chip as? Chip)?.text = it.name
            gameDetailsKeywords.addView(chip)
        }
    }

    companion object {
        val TAG: String = GameDetailsFragment::class.java.name
        private const val KEY_GAME = "game"

        fun getInstance(game: Game): GameDetailsFragment {
            return GameDetailsFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(KEY_GAME, game)
                arguments = bundle
            }
        }
    }
}
package com.lukasstancikas.gamesapp.feature.gamedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lukasstancikas.gamesapp.R
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.util.argument
import kotlinx.android.synthetic.main.fragment_game_details.*

class GameDetailsFragment : Fragment() {

    private val game: Game by argument(KEY_GAME)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailsToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        showGameDetails()
    }

    private fun showGameDetails() {
        gameDetailsSummary.text = game.summary
        if (game.cover != null) {
            gameDetailsCollapsingLayout.title = game.name
            Glide
                .with(gameDetailsCover.context)
                .load(game.cover?.trimmedCoverUrl())
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_error)
                .centerInside()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(gameDetailsCover)
        } else {
            gameDetailsToolbar.title = game.name
            gameDetailsCollapsingLayout.isTitleEnabled = false
            gameDetailsCover.visibility = View.GONE
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
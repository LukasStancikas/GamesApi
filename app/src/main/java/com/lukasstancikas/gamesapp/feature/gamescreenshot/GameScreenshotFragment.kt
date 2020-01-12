package com.lukasstancikas.gamesapp.feature.gamescreenshot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lukasstancikas.gamesapp.R
import com.lukasstancikas.gamesapp.model.Screenshot
import com.lukasstancikas.gamesapp.util.argument
import kotlinx.android.synthetic.main.fragment_game_screenshot.*

class GameScreenshotFragment : Fragment() {
    private val screenshot: Screenshot by argument(KEY_SCREENSHOT)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_screenshot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenshotToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        Glide
            .with(gameScreenshot.context)
            .load(screenshot.trimmedUrl())
            .placeholder(R.drawable.ic_photo)
            .error(R.drawable.ic_error)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(gameScreenshot)
    }

    companion object {
        val TAG: String = GameScreenshotFragment::class.java.name
        private const val KEY_SCREENSHOT = "screenshot"

        fun getInstance(screenshot: Screenshot): GameScreenshotFragment {
            return GameScreenshotFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(KEY_SCREENSHOT, screenshot)
                arguments = bundle
            }
        }
    }
}
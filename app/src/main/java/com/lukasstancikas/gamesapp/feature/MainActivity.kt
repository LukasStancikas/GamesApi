package com.lukasstancikas.gamesapp.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lukasstancikas.gamesapp.R
import com.lukasstancikas.gamesapp.feature.gamedetails.GameDetailsFragment
import com.lukasstancikas.gamesapp.feature.gamelist.GameListFragment
import com.lukasstancikas.gamesapp.feature.gamescreenshot.GameScreenshotFragment
import com.lukasstancikas.gamesapp.model.Game
import com.lukasstancikas.gamesapp.model.Screenshot
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, GameListFragment())
        transaction.commit()
    }

    fun onGameClicked(game: Game) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragmentContainer, GameDetailsFragment.getInstance(game)
        )
        transaction.addToBackStack(GameDetailsFragment.TAG)
        transaction.commit()
    }

    fun onScreenshotClicked(screenshot: Screenshot) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragmentContainer, GameScreenshotFragment.getInstance(screenshot)
        )
        transaction.addToBackStack(GameScreenshotFragment.TAG)
        transaction.commit()
    }
}

package com.lukasstancikas.gamesapp.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lukasstancikas.gamesapp.R
import com.lukasstancikas.gamesapp.feature.gamedetails.GameDetailsFragment
import com.lukasstancikas.gamesapp.feature.gamelist.GameListFragment
import com.lukasstancikas.gamesapp.model.Cover
import com.lukasstancikas.gamesapp.model.Game
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

    fun onGameClicked(gameWithCover: Pair<Game, Cover?>) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragmentContainer, GameDetailsFragment.getInstance(
                gameWithCover.first,
                gameWithCover.second
            )
        )
        transaction.addToBackStack(GameDetailsFragment.TAG)
        transaction.commit()
    }
}

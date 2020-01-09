package com.lukasstancikas.gamesapp.dagger

import com.lukasstancikas.gamesapp.feature.gamedetails.GameDetailsFragment
import com.lukasstancikas.gamesapp.feature.gamelist.GameListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeListFragment(): GameListFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): GameDetailsFragment
}
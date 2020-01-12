package com.lukasstancikas.gamesapp.dagger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukasstancikas.gamesapp.feature.gamedetails.GameDetailsViewModel
import com.lukasstancikas.gamesapp.feature.gamelist.GameListViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GameListViewModel::class)
    abstract fun bindGameListViewModel(viewModel: GameListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameDetailsViewModel::class)
    abstract fun bindGameDetailsViewModel(viewModel: GameDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
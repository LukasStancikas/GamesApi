package com.lukasstancikas.gamesapp.dagger

import com.lukasstancikas.gamesapp.model.database.AppDatabase
import com.lukasstancikas.gamesapp.network.Api
import com.lukasstancikas.gamesapp.network.RepositoryController
import com.lukasstancikas.gamesapp.network.RepositoryControllerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ControllerModule {

    @Singleton
    @Provides
    fun provideApi(api: Api, appDatabase: AppDatabase): RepositoryController =
        RepositoryControllerImpl(api, appDatabase)
}
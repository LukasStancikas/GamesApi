package com.lukasstancikas.gamesapp.dagger

import com.lukasstancikas.gamesapp.network.ApiController
import com.lukasstancikas.gamesapp.network.ApiControllerImpl
import com.lukasstancikas.gamesapp.network.Api
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ControllerModule {

    @Singleton
    @Provides
    fun provideApi(api: Api): ApiController =
        ApiControllerImpl(api)
}
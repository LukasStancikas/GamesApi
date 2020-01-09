package com.lukasstancikas.gamesapp.model

sealed class NetworkRequest {
    object Loading : NetworkRequest()
    object Done : NetworkRequest()
    data class Error(val throwable: Throwable) : NetworkRequest()
}
package com.lukasstancikas.gamesapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cover(val url: String, val game: String) : Parcelable {
    fun trimmedCoverUrl(): String {
        return if (url.startsWith("//")) {
            "https:$url"
        } else {
            url
        }
    }
}
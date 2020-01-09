package com.lukasstancikas.gamesapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Cover(
    @PrimaryKey val id: Int,
    val url: String,
    val game: Int
) : Parcelable {
    fun trimmedCoverUrl(): String {
        return if (url.startsWith("//")) {
            "https:$url"
        } else {
            url
        }
    }
}
package com.lukasstancikas.gamesapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Screenshot(
    @PrimaryKey val id: Long,
    val url: String,
    val game: Int
) : Parcelable {
    fun trimmedUrl(): String {
        return if (url.startsWith("//")) {
            "https:$url"
        } else {
            url
        }
    }
}
package com.lukasstancikas.gamesapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    val id: String,
    val name: String,
    val rating: Double,
    val summary: String,
    @SerializedName("cover")
    val coverID: String
) : Parcelable
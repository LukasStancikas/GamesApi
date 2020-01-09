package com.lukasstancikas.gamesapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Game(
    @PrimaryKey val id: Int,
    val name: String,
    val rating: Double,
    val summary: String,
    @Ignore val cover: Cover?
) : Parcelable {

    constructor(id: Int, name: String, rating: Double, summary: String) : this(
        id,
        name,
        rating,
        summary,
        null
    )
}
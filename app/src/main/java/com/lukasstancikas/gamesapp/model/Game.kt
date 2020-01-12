package com.lukasstancikas.gamesapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Game(
    @PrimaryKey val id: Long,
    val name: String,
    val rating: Double = 0.0,
    val summary: String?,
    @Ignore val cover: Cover?,
    @SerializedName("keywords")
    val keywordIds: List<Long>?
) : Parcelable {

    constructor(
        id: Long,
        name: String,
        rating: Double,
        summary: String?,
        keywordIds: List<Long>?
    ) : this(
        id,
        name,
        rating,
        summary,
        null,
        keywordIds
    )
}
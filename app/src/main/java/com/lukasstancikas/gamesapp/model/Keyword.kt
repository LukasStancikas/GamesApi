package com.lukasstancikas.gamesapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Keyword(
    @PrimaryKey val id: Long,
    val name: String
) : Parcelable
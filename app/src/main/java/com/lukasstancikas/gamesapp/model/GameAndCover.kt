package com.lukasstancikas.gamesapp.model

import androidx.room.Embedded
import androidx.room.Relation

data class GameAndCover(
    @Embedded val game: Game,
    @Relation(
        parentColumn = "id",
        entityColumn = "game"
    )
    var cover: Cover? = null
)
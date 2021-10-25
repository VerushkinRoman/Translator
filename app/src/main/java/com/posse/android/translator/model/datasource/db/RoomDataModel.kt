package com.posse.android.translator.model.datasource.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomDataModel(

    @PrimaryKey val word: String,
    val meaning: String?
)
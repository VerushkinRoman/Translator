package com.posse.android.data.datasource.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomDataModel(

    @PrimaryKey val word: String,
    val meaning: String?,
    var timestamp: Long,
    val imageUrl: String?
)
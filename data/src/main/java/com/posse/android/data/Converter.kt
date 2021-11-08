package com.posse.android.data

import com.posse.android.models.DataModel
import com.posse.android.models.Meanings
import com.posse.android.models.Translation
import com.posse.android.data.datasource.db.RoomDataModel

fun convertRoomModelToDataModel(roomModel: RoomDataModel) =
    DataModel(
        roomModel.word,
        listOf(
            Meanings(
                Translation(roomModel.meaning),
                roomModel.imageUrl
            )
        )
    )

fun convertDataModelToRoomModel(dataModel: DataModel) =
    RoomDataModel(
        dataModel.text?.lowercase() ?: "",
        dataModel.meanings?.get(0)?.translation?.translation,
        0,
        dataModel.meanings?.get(0)?.imageUrl
    )
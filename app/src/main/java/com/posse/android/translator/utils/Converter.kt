package com.posse.android.translator.utils

import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.data.Meanings
import com.posse.android.translator.model.data.Translation
import com.posse.android.translator.model.datasource.db.RoomDataModel

fun convertRoomModelToDataModel(roomModel: RoomDataModel) =
    DataModel(roomModel.word, listOf(Meanings(Translation(roomModel.meaning), roomModel.imageUrl)))

fun convertDataModelToRoomModel(dataModel: DataModel) =
    RoomDataModel(
        dataModel.text?.lowercase() ?: "",
        dataModel.meanings?.get(0)?.translation?.translation,
        0,
        dataModel.meanings?.get(0)?.imageUrl
    )
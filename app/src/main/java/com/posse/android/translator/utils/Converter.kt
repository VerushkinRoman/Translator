package com.posse.android.translator.utils

import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.data.Meanings
import com.posse.android.translator.model.data.Translation
import com.posse.android.translator.model.datasource.db.RoomDataModel
import java.util.*

fun convertRoomModelToDataModel(roomModel: RoomDataModel) =
    DataModel(roomModel.word, listOf(Meanings(Translation(roomModel.meaning), null)))

fun convertDataModelToRoomModel(dataModel: DataModel) =
    RoomDataModel(dataModel.text?.lowercase() ?: "", dataModel.meanings?.get(0)?.translation?.translation)
package com.posse.android.test

import com.posse.android.data.convertDataModelToRoomModel
import com.posse.android.data.convertRoomModelToDataModel
import com.posse.android.data.datasource.db.RoomDataModel
import com.posse.android.models.DataModel
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Test

import org.junit.Assert.*

class ConverterTest {

    @Test
    fun converter_RoomDataModel_ReturnsDataModel(){
        val roomDataModel = RoomDataModel("test", null, 123, null)
        val testClass = convertRoomModelToDataModel(roomDataModel)
        assertThat(testClass, instanceOf(DataModel::class.java))
    }

    @Test
    fun converter_DataModel_ReturnsRoomDataModel(){
        val dataModel = DataModel("test", null)
        val testClass = convertDataModelToRoomModel(dataModel)
        assertThat(testClass, instanceOf(RoomDataModel::class.java))
    }
}
package com.posse.android.test

import com.posse.android.adapter.MainAdapter
import com.posse.android.models.DataModel
import org.junit.Assert
import org.junit.Test

class AdapterTest {

    private val items: List<DataModel> = listOf()

    private val adapter = MainAdapter(
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) = Unit
        },
        items
    )

    @Test
    fun adapter_ItemCount_ReturnsDataSize() {
        Assert.assertEquals(items.size, adapter.itemCount)
    }
}
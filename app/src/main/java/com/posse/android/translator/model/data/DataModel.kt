package com.posse.android.translator.model.data

import com.google.gson.annotations.SerializedName
import com.posse.android.translator.model.data.Meanings

class DataModel(
    @field:SerializedName("text") val text: String?,
    @field:SerializedName("meanings") val meanings: List<Meanings>?
)

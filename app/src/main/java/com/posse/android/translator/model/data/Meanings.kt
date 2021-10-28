package com.posse.android.translator.model.data

import com.google.gson.annotations.SerializedName
import com.posse.android.translator.model.data.Translation

class Meanings(
    @field:SerializedName("translation") val translation: Translation?,
    @field:SerializedName("imageUrl") val imageUrl: String?
)

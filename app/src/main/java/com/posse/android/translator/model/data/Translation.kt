package com.posse.android.translator.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Translation(@SerializedName("text") val translation: String?) : Parcelable

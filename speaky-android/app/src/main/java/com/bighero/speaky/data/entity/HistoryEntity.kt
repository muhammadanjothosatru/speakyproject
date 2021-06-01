package com.bighero.speaky.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryEntity (
    val title: String = "",
    val point: Long = 12,
    val tanggal: String = ""
) : Parcelable
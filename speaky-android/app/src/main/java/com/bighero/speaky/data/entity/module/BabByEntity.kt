package com.bighero.speaky.data.entity.module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BabByEntity(
    val key: String,
    val no: String,
    val time: Long,
    val konten: String,
    val judul: String,
    val video: String,
    val practice: List<practices>,
) : Parcelable {
    @Parcelize
    data class practices(
        val key: String,
        val time: Long
    ) : Parcelable
}


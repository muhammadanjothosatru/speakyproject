package com.bighero.speaky.data.source.remote.response

import android.os.Parcelable
import com.bighero.speaky.data.entity.module.ModuleEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class APackResponse(
    val id: String,
    val title: String,
    val type: String,
    val instruction: String
) : Parcelable {
}
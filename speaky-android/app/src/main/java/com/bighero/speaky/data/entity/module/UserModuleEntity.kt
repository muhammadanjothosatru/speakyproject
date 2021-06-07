package com.bighero.speaky.data.entity.module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModuleEntity(
    val key : String,
    val bab : Bab
) : Parcelable {
    @Parcelize
    data class Bab(
        val key: String,
        val status: String,
    ) : Parcelable

}

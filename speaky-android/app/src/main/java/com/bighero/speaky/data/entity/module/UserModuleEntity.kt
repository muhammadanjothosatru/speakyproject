package com.bighero.speaky.data.entity.module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModuleEntity(
    val key : String,
) : Parcelable

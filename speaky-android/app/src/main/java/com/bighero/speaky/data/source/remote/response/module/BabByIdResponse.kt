package com.bighero.speaky.data.source.remote.response.module

import android.os.Parcelable
import com.bighero.speaky.data.entity.module.BabByEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BabByIdResponse(
    var module: BabByEntity? = null,
    var exception: Exception? = null
) : Parcelable
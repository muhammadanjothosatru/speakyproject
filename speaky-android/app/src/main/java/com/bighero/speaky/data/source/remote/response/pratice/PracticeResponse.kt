package com.bighero.speaky.data.source.remote.response.pratice

import android.os.Parcelable
import com.bighero.speaky.data.entity.pratice.PraticeEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PracticeResponse(
    var pratice: List<PraticeEntity>? = null,
    var exception: Exception? = null
) : Parcelable
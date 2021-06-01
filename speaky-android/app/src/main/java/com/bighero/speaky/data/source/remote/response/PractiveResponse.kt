package com.bighero.speaky.data.source.remote.response

import android.os.Parcelable
import com.bighero.speaky.data.entity.HistoryEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PractiveResponse (
    var history: List<HistoryEntity>? = null,
    var exception: Exception? = null
) : Parcelable
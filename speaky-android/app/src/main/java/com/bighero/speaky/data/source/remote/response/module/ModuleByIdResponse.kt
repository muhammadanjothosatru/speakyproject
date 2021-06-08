package com.bighero.speaky.data.source.remote.response.module

import android.os.Parcelable
import com.bighero.speaky.data.entity.HistoryEntity
import com.bighero.speaky.data.entity.module.ModuleEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModuleByIdResponse(
    var module: ModuleEntity? = null,
    var exception: Exception? = null
) : Parcelable {
}
package com.bighero.speaky.data.source.remote.response

import android.os.Parcelable
import com.bighero.speaky.data.entity.module.ModuleEntity
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ModuleResponse (
    var module: List<ModuleEntity>? = null,
    var exception: Exception? = null
    ) : Parcelable
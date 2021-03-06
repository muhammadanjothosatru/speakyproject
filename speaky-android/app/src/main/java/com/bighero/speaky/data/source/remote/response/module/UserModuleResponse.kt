package com.bighero.speaky.data.source.remote.response.module

import android.os.Parcelable
import com.bighero.speaky.data.entity.module.UserModuleEntity
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserModuleResponse(
    var UserModule: List<UserModuleEntity>? = null,
    var exception: Exception? = null
) : Parcelable
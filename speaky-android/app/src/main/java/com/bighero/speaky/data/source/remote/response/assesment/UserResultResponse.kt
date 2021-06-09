package com.bighero.speaky.data.source.remote.response.assesment

import android.os.Parcelable
import com.bighero.speaky.data.entity.assesment.AssessmentEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResultResponse(
    var history: AssessmentEntity? = null,
    var exception: Exception? = null
) : Parcelable
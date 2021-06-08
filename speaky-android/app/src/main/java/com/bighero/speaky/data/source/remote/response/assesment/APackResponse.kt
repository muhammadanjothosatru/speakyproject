package com.bighero.speaky.data.source.remote.response.assesment

import android.os.Parcelable
import com.bighero.speaky.data.entity.assesment.AssessmentPackEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class APackResponse(
    var pack: List<AssessmentPackEntity>? = null,
    var exception: Exception? = null
) : Parcelable
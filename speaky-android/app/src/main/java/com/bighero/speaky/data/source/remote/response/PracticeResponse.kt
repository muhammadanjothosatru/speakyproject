package com.bighero.speaky.data.source.remote.response

import android.os.Parcelable
import com.bighero.speaky.data.entity.PraticeEntity
import com.bighero.speaky.data.entity.assesment.AssessmentPackEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PracticeResponse(
    var pratice: List<PraticeEntity>? = null,
    var exception: Exception? = null
) : Parcelable
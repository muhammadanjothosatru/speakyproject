package com.bighero.speaky.data.source.remote.response

import android.os.Parcelable
import com.bighero.speaky.data.entity.AssessmentEntity
import com.bighero.speaky.data.entity.HistoryEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserAssesmentResponse (
    var history: List<AssessmentEntity>? = null,
    var exception: Exception? = null
) : Parcelable
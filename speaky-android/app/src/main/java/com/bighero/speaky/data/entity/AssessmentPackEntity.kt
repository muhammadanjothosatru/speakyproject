package com.bighero.speaky.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssessmentPackEntity(
    var name: String,
    var guide: String
        ): Parcelable
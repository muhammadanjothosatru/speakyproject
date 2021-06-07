package com.bighero.speaky.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AssessmentEntity(
    val donwloadUrl : String
) : Parcelable

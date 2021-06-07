package com.bighero.speaky.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AssessmentEntity(
    val donwloadUrl : String,
    val score : Long,
    val timeStamp : String,
    val blink : Long,
    val disfluency : Long,
    val gaze : Long
) : Parcelable

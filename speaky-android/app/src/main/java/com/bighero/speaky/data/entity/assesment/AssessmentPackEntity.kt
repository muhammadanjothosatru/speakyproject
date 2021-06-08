package com.bighero.speaky.data.entity.assesment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssessmentPackEntity(
    var id : String,
    var title: String,
    var type: String,
    var petunjuk : String,
    var guide : List<String>
    ): Parcelable
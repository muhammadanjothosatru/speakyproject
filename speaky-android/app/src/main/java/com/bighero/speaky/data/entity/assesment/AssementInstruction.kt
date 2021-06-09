package com.bighero.speaky.data.entity.assesment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AssementInstruction(
    var type: String,
    var intruksi: List<String>
) : Parcelable
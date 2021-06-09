package com.bighero.speaky.data.source.remote.response.assesment

import android.os.Parcelable
import com.bighero.speaky.data.entity.assesment.AssementInstruction
import kotlinx.android.parcel.Parcelize

@Parcelize
class InstructionResponse(
    var intruction: AssementInstruction? = null,
    var exception: Exception? = null
) : Parcelable
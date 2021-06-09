package com.bighero.speaky.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PraticeEntity (
    var key :String,
    var judul : String,
    var cover : String,
    var ilustrasi : Ilustration
        ) : Parcelable {
    @Parcelize
    class Ilustration (
        val durasi : Long,
        val link : String
            ) : Parcelable
}
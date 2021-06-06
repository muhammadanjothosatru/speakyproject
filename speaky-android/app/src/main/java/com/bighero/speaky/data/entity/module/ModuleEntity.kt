package com.bighero.speaky.data.entity.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ModuleEntity(
    val gambar : String,
    val judul : String,
    val bab : Bab
) : Parcelable {
    @Parcelize
    data class Bab(
        val gambar: String,
        val judul: String,
        val video: String,
        val bab1: babdetail,
        val bab2: babdetail,
    ) : Parcelable {
        @Parcelize
        data class babdetail(
            val konten: String,
            val judul: String,
            val practice: practices,
        ) : Parcelable {
            @Parcelize
            data class practices (
                val PRA0001 : praticedetail,
                val PRA0002 : praticedetail,
                val PRA0005 : praticedetail,
            ) : Parcelable {
                @Parcelize
                data class praticedetail (
                    val time : Int
                        ) : Parcelable
            }
        }
    }
}


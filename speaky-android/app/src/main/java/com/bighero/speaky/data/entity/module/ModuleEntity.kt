package com.bighero.speaky.data.entity.module

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ModuleEntity(
    val key : String,
    val deskripsi : String,
    val gambar: String,
    val judul: String,
    val bab : List< Bab>
) : Parcelable {
    @Parcelize
    data class Bab(
        val konten: String,
        val judul: String,
        val video: String,
       val practice: List<practices>,
    ) : Parcelable
        {
            @Parcelize
            data class practices (
                val key : String,
                val time : Long
            ) : Parcelable

//        }
    }
}


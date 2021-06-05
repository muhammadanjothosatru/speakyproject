package com.bighero.speaky.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AssesementResponse(
    @field:SerializedName("timestamp")
    val timestamp: String,

    @field:SerializedName("score")
    val score:Long ,

    @field:SerializedName("blink")
    val blink: Blink,

    @field:SerializedName("disfluency")
    val disfluency:Disfluency,

    @field:SerializedName("urlvideo")
    val urlvideo:String ,
) {
    data class Disfluency (
        @field:SerializedName("value")
        val value:Long , )

    data class Blink (
        @field:SerializedName("value")
        val value:Long , )
}



package com.bighero.speaky.data.source.remote.network

import com.bighero.speaky.data.source.remote.response.AssesementResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api")
    fun getAssessment(
        @Query("link") link: String,
        @Query("uid") uid: String
    ): Call<AssesementResponse>
}
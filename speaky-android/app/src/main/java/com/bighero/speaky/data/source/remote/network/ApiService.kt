package com.bighero.speaky.data.source.remote.network

import com.bighero.speaky.data.source.remote.response.AssesementResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users/{username}")
    suspend fun getAssesmentUser(
        @Path("username") query: String,
        @Query("Authorization") accessToken: String,
        @Query("autocomplete") autoComplete: Boolean = true
    ): AssesementResponse
}
package com.studentjobs.app.firebase.location

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("search")

    suspend fun searchAddress(

        @Query("q")
        address: String,

        @Query("format")
        format: String = "json",

        @Query("limit")
        limit: Int = 1

    ): List<GeocodingResponse>
}
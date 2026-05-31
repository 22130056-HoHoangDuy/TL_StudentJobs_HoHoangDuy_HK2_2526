package com.studentjobs.app.firebase.location

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeocodingService {

    private val api =

        Retrofit.Builder()

            .baseUrl(
                "https://nominatim.openstreetmap.org/"
            )

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()

            .create(
                GeocodingApi::class.java
            )

    suspend fun getLatLng(

        address: String

    ): Pair<Double, Double>? {

        return try {

            val result =

                api.searchAddress(
                    address
                )

            if (result.isEmpty()) {

                null

            } else {

                Pair(

                    result[0].lat.toDouble(),

                    result[0].lon.toDouble()
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }
}
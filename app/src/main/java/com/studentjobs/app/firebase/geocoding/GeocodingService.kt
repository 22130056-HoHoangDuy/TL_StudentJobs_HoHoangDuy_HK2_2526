package com.studentjobs.app.firebase.geocoding

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeocodingService {

    private val api: GeocodingApi

    init {

        val client =

            OkHttpClient.Builder()

                .addInterceptor { chain ->

                    val request =

                        chain.request()

                            .newBuilder()

                            .header(
                                "User-Agent",
                                "StudentJobs-App/1.0"
                            )

                            .header(
                                "Accept",
                                "application/json"
                            )

                            .build()

                    chain.proceed(request)
                }

                .build()

        api =

            Retrofit.Builder()

                .baseUrl(
                    "https://nominatim.openstreetmap.org/"
                )

                .client(client)

                .addConverterFactory(
                    GsonConverterFactory.create()
                )

                .build()

                .create(
                    GeocodingApi::class.java
                )
    }

    suspend fun getLatLng(

        address: String

    ): Pair<Double, Double>? {

        return try {

            val result =

                api.searchAddress(
                    address
                )
            println("ADDRESS = $address")
            println("RAW RESULT = $result")
            if (result.isEmpty()) {

                null

            } else {

                Pair(

                    result[0].lat.toDouble(),

                    result[0].lon.toDouble()
                )
            }


        } catch (e: Exception) {

            println("GEOCODING ERROR = ${e.message}")

            e.printStackTrace()

            null
        }
    }
}
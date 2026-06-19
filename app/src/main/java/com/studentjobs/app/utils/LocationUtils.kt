package com.studentjobs.app.utils

import android.content.Context
import android.location.Geocoder
import java.util.Locale

object LocationUtils {
    fun getReadableAddress(context: Context, lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale("vi", "VN"))
            val addresses = geocoder.getFromLocation(lat, lng, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val ward = address.subLocality
                val district = address.subAdminArea
                val city = address.locality

                val result = StringBuilder()
                if (!ward.isNullOrBlank()) result.append(ward)
                if (!district.isNullOrBlank()) {
                    if (result.isNotEmpty()) result.append(", ")
                    result.append(district)
                }
                if (result.isEmpty() && !city.isNullOrBlank()) {
                    result.append(city)
                }
                if (result.isNotEmpty()) result.toString() else "Tọa độ: $lat, $lng"
            } else {
                "Vị trí chưa được xác định"
            }
        } catch (e: Exception) {
            "Tọa độ: $lat, $lng"
        }
    }
}
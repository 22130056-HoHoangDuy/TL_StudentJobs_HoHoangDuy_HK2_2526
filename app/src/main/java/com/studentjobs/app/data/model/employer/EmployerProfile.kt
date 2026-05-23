package com.studentjobs.app.data.model.employer

data class EmployerProfile(

    // uid
    val uid: String = "",

    // business information
    val businessName: String = "",
    val businessCategory: String? = null,
    val businessDescription: String? = null,

    // location
    val businessAddressText: String? = null,
    val businessLocationUrl: String? = null,
    val businessLatitude: Double? = null,
    val businessLongitude: Double? = null,

    // media
    val businessLogoUrl: String? = null,
    val businessStoreFrontImageUrl: String? = null,

    // system log
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
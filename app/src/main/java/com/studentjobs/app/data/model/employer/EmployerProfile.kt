package com.studentjobs.app.data.model.employer

import java.util.Date

data class EmployerProfile(

    val uid: String = "",

    val businessName: String = "",

    val businessCategory: String? = null,

    val businessDescription: String? = null,

    val businessAddressText: String? = null,

    val businessLocationUrl: String? = null,

    val businessLatitude: Double? = null,

    val businessLongitude: Double? = null,

    val businessLogoUrl: String? = null,

    val businessStoreFrontImageUrl: String? = null,

    val createdAt: Date? = null,

    val updatedAt: Date? = null,

    val tempAvatarUri: String? = null
)
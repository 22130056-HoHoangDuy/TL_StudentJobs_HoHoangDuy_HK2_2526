package com.studentjobs.app.data.model.employer

data class EmployerProfile(

    // ===== ID =====
    val uid: String = "",

    // ===== BUSINESS =====
    val businessName: String = "",

    val businessCategory: String? = null,

    val businessDescription: String? = null,

    // ===== LOCATION =====
    val businessAddress: String? = null,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val googleMapsUrl: String? = null,

    // ===== MEDIA =====
    val logoUrl: String? = null,

    val storeFrontImageUrl: String? = null,

    // ===== PROFILE STATUS =====
    val profileCompleted: Boolean = false,

    // ===== SYSTEM =====
    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)
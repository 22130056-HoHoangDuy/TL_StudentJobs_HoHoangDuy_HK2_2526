package com.studentjobs.app.data.model

data class User(
    // ===== BASIC =====
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.STUDENT,

    // ===== VERIFY STATUS =====
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val isStudentVerified: Boolean = false,
    val isBusinessVerified: Boolean = false,
    val isStudentEmailVerified: Boolean = false,

    // ===== OCR DATA =====
    val extractedName: String? = null,
    val studentId: String? = null,
    val school: String? = null,
    val dateOfBirth: String? = null,

    // ===== CONTACT =====
    val phoneNumber: String? = null,
    val studentEmail: String? = null,

    // ===== PROFILE =====
    val avatarUrl: String? = null,
    val bio: String? = null,

    // ===== CAREER =====
    val major: String? = null,
    val skills: List<String> = emptyList(),

    // ===== SYSTEM =====
    val trustScore: Int = 0,
    // ===== EMPLOYER =====
    val businessName: String? = null,
    val businessAddress: String? = null,
    val businessCategory: String? = null,
    val businessLicenseUrl: String? = null,
    val storeFrontImageUrl: String? = null,
    val verificationStatus: String = "UNVERIFIED",

    // ==== INFORMATION FROM EMPLOYER ==== //
    val businessLatitude: Double? = null,
    val businessLongitude: Double? = null,
    val googleMapsUrl: String? = null,
    val businessDescription: String? = null,
)
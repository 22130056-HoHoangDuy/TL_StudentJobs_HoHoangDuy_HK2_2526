package com.studentjobs.app.feature.profile

import com.studentjobs.app.data.model.UserRole

data class ProfileUiState(

    // ===== SYSTEM =====
    val isLoading: Boolean = true,

    // ===== ROLE =====
    val role: UserRole = UserRole.STUDENT,

    // ===== BASIC =====
    val name: String = "",
    val email: String = "",
    val avatarUrl: String = "",

    // ===== VERIFICATION =====
    val isStudentVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val isEmailVerified: Boolean = false,
    val isStudentEmailVerified: Boolean = false,
    val isBusinessVerified: Boolean = false,

    // ===== OCR DATA =====
    val extractedName: String = "",
    val studentId: String = "",
    val school: String = "",
    val dateOfBirth: String = "",

    // ===== CONTACT =====
    val phone: String = "",
    val studentEmail: String = "",

    // ===== PROFILE =====
    val bio: String = "",
    val major: String = "",

    // ===== SKILLS =====
    val skills: List<String> = emptyList(),

    // ===== TRUST =====
    val trustScore: Int = 0,

    // ===== EMPLOYER =====
    val businessName: String = "",
    val businessCategory: String = "",
    val businessAddress: String = "",
    val businessDescription: String = "",
    val googleMapsUrl: String = "",

    val businessLicenseUrl: String = "",
    val storeFrontImageUrl: String = ""
)
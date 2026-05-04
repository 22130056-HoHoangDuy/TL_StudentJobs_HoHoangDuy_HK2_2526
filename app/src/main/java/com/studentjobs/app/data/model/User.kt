package com.studentjobs.app.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.STUDENT,

    // ===== VERIFY STATUS =====
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val isStudentVerified: Boolean = false,
    val isBusinessVerified: Boolean = false,

    // ===== OCR DATA =====
    val extractedName: String? = null,
    val studentId: String? = null,
    val school: String? = null,
    val dateOfBirth: String? = null,
    // ===== MEDIA =====
    val avatarUrl: String? = null
)
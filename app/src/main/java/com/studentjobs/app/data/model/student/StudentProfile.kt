package com.studentjobs.app.data.model.student

data class StudentProfile(

    // ===== ID =====
    val uid: String = "",

    // ===== BASIC PROFILE =====
    val fullName: String = "",

    val avatarUrl: String? = null,

    val bio: String? = null,

    // ===== ACADEMIC =====
    val school: String? = null,

    val major: String? = null,

    val studentEmail: String? = null,

    // ===== SKILLS =====
    val skills: List<String> = emptyList(),

    // ===== LOCATION =====
    val latitude: Double? = null,

    val longitude: Double? = null,

    val address: String? = null,

    // ===== JOB PREFERENCE =====
    val preferredJobCategories: List<String> =
        emptyList(),

    val preferredSalaryMin: Double? = null,

    // ===== PROFILE STATUS =====
    val profileCompleted: Boolean = false,

    // ===== SYSTEM =====
    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)
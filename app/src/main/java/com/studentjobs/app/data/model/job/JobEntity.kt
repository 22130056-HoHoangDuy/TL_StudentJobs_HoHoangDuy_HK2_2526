package com.studentjobs.app.data.model.job

data class JobEntity(

    // ===== ID =====
    val jobId: String = "",

    // ===== OWNER =====
    val employerUid: String = "",

    // ===== BASIC =====
    val title: String = "",
    val description: String = "",

    val category: String = "",

    // ===== SALARY =====
    val salaryMin: Double = 0.0,
    val salaryMax: Double = 0.0,

    // ===== LOCATION =====
    val locationText: String = "",

    val latitude: Double? = null,
    val longitude: Double? = null,

    // ===== REQUIREMENTS =====
    val requiredSkills: List<String> = emptyList(),

    // ===== RECRUITMENT =====
    val autoRecruitmentEnabled: Boolean = false,

    // ===== MODERATION =====
    val moderationStatus: String = "PENDING",

    // ===== STATUS =====
    val status: String = "ACTIVE",

    // ===== SYSTEM =====
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
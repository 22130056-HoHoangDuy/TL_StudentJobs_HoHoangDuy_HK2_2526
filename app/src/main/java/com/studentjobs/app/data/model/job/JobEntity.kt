package com.studentjobs.app.data.model.job

import java.util.Date

data class JobEntity(

    // ===== ID =====
    val jobId: String = "",

    // ===== OWNER =====
    val employerUid: String = "",

    val businessName: String = "",

    // ===== BASIC =====
    val title: String = "",
    val description: String = "",

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

    // ===== STATUS =====
    val status: String = "ACTIVE",
    //
    val requiredApplicants: Int = 1,
    val currentApplicants: Int = 0,

    //job category
    val businessCategory: String = "",

    // accepted applicants
    val acceptedApplicants: Int = 0,

    // ===== SYSTEM =====
    val createdAt: Date?= null,
    val updatedAt: Date? = null


)
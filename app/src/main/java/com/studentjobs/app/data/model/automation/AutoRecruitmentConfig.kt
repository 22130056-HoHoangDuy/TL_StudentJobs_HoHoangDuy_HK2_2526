package com.studentjobs.app.data.model.automation

data class AutoRecruitmentConfig(

    // ===== OWNER =====
    val employerUid: String = "",

    // ===== STATUS =====
    val enabled: Boolean = false,

    // ===== FILTER =====
    val verifiedStudentsOnly: Boolean = true,

    val minimumTrustScore: Int = 50,

    // ===== RECRUITMENT =====
    val minimumApplicants: Int = 3,

    val autoRejectRemaining: Boolean = true,

    // ===== MATCHING =====
    val skillMatchingEnabled: Boolean = true,

    val locationMatchingEnabled: Boolean = true,

    // ===== SYSTEM =====
    val updatedAt: Long = 0L
)
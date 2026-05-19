package com.studentjobs.app.data.model.trust

data class TrustLog(

    // ===== ID =====
    val trustLogId: String = "",

    // ===== RELATION =====
    val userUid: String = "",

    // ===== TRUST ACTION =====
    val actionType: String = "",

    val changeAmount: Int = 0,

    val severity: String = "LOW",

    // ===== DESCRIPTION =====
    val description: String? = null,

    // ===== SYSTEM =====
    val createdAt: Long = 0L
)
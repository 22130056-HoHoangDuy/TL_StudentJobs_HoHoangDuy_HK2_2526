package com.studentjobs.app.data.model.trust

data class Penalty(

    // ===== ID =====
    val penaltyId: String = "",

    // ===== RELATION =====
    val userUid: String = "",

    // ===== PENALTY =====
    val reason: String = "",

    val severity: String = "LOW",

    val scoreDeducted: Int = 0,

    // ===== STATUS =====
    val active: Boolean = true,

    // ===== SYSTEM =====
    val createdAt: Long = 0L
)
package com.studentjobs.app.data.model.trust

import java.util.Date

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
    val createdAt: Date? = null
)
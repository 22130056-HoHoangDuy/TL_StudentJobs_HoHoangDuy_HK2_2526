package com.studentjobs.app.data.model.subscription

data class SubscriptionRequest(

    // ===== ID =====
    val requestId: String = "",

    // ===== RELATION =====
    val userUid: String = "",

    // ===== SUBSCRIPTION =====
    val requestedPlan: String = "PLUS",

    val durationDays: Int = 30,

    // ===== PAYMENT =====
    val paymentMethod: String = "QR",

    val paymentProofUrl: String? = null,

    // ===== STATUS =====
    val status: String = "PENDING",

    // ===== ADMIN =====
    val reviewedBy: String? = null,
    val reviewNote: String? = null,

    // ===== SYSTEM =====
    val requestedAt: Long = 0L,
    val reviewedAt: Long? = null
)
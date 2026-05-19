package com.studentjobs.app.data.model.job

data class JobModeration(

    // ===== ID =====
    val moderationId: String = "",

    // ===== RELATION =====
    val jobId: String = "",

    // ===== MODERATION =====
    val flaggedReason: String = "",

    val moderationStatus: String = "PENDING",

    // ===== ADMIN =====
    val reviewedBy: String? = null,
    val reviewNote: String? = null,

    // ===== SYSTEM =====
    val createdAt: Long = 0L,
    val reviewedAt: Long? = null
)
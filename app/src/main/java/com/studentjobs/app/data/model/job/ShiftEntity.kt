package com.studentjobs.app.data.model.job

data class ShiftEntity(

    // ===== ID =====
    val shiftId: String = "",

    // ===== RELATION =====
    val jobId: String = "",

    // ===== SHIFT =====
    val dayOfWeek: String = "",

    val startTime: String = "",
    val endTime: String = "",

    // ===== SLOT =====
    val slots: Int = 0,

    // ===== STATUS =====
    val status: String = "ACTIVE",

    // ===== SYSTEM =====
    val createdAt: Long = 0L
)
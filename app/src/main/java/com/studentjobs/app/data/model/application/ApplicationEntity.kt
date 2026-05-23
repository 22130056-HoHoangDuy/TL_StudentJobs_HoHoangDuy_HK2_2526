package com.studentjobs.app.data.model.application

data class ApplicationEntity(

    // ===== ID =====
    val applicationId: String = "",

    // ===== RELATION =====
    val studentUid: String = "",
    val employerUid: String = "",

    val jobId: String = "",
    val shiftId: String = "",

    // ===== STATUS =====
    val status: String = "PENDING",

    // ===== CONFLICT =====
    val conflictDetected: Boolean = false,

    // ===== SNAPSHOT =====
    val jobTitle: String = "",
    val businessName: String = "",

    val shiftStartTime: String = "",
    val shiftEndTime: String = "",

    // ===== SYSTEM =====
    val appliedAt: Long = 0L
)
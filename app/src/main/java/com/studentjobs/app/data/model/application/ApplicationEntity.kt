package com.studentjobs.app.data.model.application

data class ApplicationEntity(

    // ID
    val applicationId: String = "",

    // Relation
    val studentUid: String = "",

    val employerUid: String = "",

    val jobId: String = "",

    // Status
    val status: String = "PENDING",

    // Schedule matching
    val conflictDetected: Boolean = false,

    // Student Snapshot
    val studentName: String = "",

    val schoolName: String = "",

    // Job Snapshot
    val jobTitle: String = "",

    // Employer Snapshot
    val businessName: String = "",

    // System
    val appliedAt: Long = 0L
)
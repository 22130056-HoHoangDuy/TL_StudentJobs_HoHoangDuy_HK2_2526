package com.studentjobs.app.data.model.application

import java.util.Date

data class ApplicationEntity(

    val applicationId: String = "",

    val studentUid: String = "",

    val employerUid: String = "",

    val jobId: String = "",

    val status: String = "PENDING",

    val conflictDetected: Boolean = false,

    val studentName: String = "",

    val schoolName: String = "",

    val jobTitle: String = "",

    val businessName: String = "",

    val appliedAt: Date? = null
)

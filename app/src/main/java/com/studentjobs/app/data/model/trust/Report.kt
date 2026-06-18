package com.studentjobs.app.data.model.trust

import java.util.Date

data class Report(

    // ===== ID =====
    val reportId: String = "",

    // ===== RELATION =====
    val reporterUid: String = "",
    val reportedUserUid: String = "",

    val jobId: String? = null,

    // ===== REPORT =====
    val reason: String = "",

    val description: String? = null,

    // ===== EVIDENCE =====
    val evidences: List<ReportEvidence> = emptyList(),

    // ===== STATUS =====
    val status: String = "PENDING",

    // ===== ADMIN =====
    val reviewedBy: String? = null,
    val reviewNote: String? = null,

    // ===== SYSTEM =====
    val createdAt: Date? = null,
    val reviewedAt: Date? = null
)

data class ReportEvidence(

    val type: String = "",

    val fileUrl: String = ""
)
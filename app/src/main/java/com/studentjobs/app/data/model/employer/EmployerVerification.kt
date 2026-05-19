package com.studentjobs.app.data.model.employer

data class EmployerVerification(
// ===== ID =====
    val uid: String = "",
// ===== BUSINESS LICENSE =====
    val businessLicenseUrl: String? = null,
// ===== BUSINESS INFO =====
    val businessName: String? = null,
    val businessCategory: String? = null,
    val businessAddress: String? = null,
// ===== MAP =====
    val googleMapsUrl: String? = null,
// ===== MEDIA =====
    val storeFrontImageUrl: String? = null,
// ===== VERIFICATION =====
    val isBusinessVerified: Boolean = false,
    val verificationStatus: String = "PENDING",
// ===== ADMIN ===== val reviewedBy: String? = null,
    val reviewNote: String? = null,
// ===== SYSTEM =====
    val submittedAt: Long = 0L,
    val reviewedAt: Long? = null
)
package com.studentjobs.app.data.model.student

data class StudentVerification(
// ===== ID =====
    val uid: String = "",

// ===== OCR RESULT =====
    val extractedStudentName: String? = null,
    val extractedStudentId: String? = null,
    val extractedSchool: String? = null,
    val extractedDob: String? = null,

// ===== CARD IMAGES =====
    val studentCardFrontUrl: String? = null,
    val studentCardBackUrl: String? = null,

// ===== EMAIL VERIFICATION =====
    val studentEmail: String? = null,
    val isStudentEmailVerified: Boolean = false,

// ===== VERIFICATION =====
    val isStudentVerified: Boolean = false,
    val verificationStatus: String = "PENDING",

// ===== SYSTEM =====
    val submittedAt: Long = 0L,
    val verifiedAt: Long? = null
)
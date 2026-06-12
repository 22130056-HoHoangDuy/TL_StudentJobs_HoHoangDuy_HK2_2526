package com.studentjobs.app.data.model.student

import com.studentjobs.app.data.model.status.VerificationStatus

data class StudentVerification(

    // uid
    val uid: String = "",

    // OCR result
    val extractedStudentName: String? = null,
    val extractedStudentId: String? = null,
    val extractedStudentSchoolName: String? = null,
    val extractedStudentDob: String? = null,
    val extractedStudentMajor: String? = null,

    // student card image
    val studentCardFrontUrl: String? = null,
    val studentCardBackUrl: String? = null,

    // verification
    val studentCardVerified: VerificationStatus =
        VerificationStatus.UNVERIFIED,

    val studentEmailVerified: VerificationStatus =
        VerificationStatus.UNVERIFIED,

    val studentPhoneVerified: VerificationStatus =
        VerificationStatus.UNVERIFIED,

    // system
    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)
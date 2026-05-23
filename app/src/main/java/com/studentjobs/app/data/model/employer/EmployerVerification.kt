package com.studentjobs.app.data.model.employer

import com.studentjobs.app.data.model.status.VerificationStatus

data class EmployerVerification(
    // uid
    val uid: String = "",

    // business license
    val businessLicenseUrl: String? = null,

    // business information
    val businessName: String? = null,
    val businessCategory: String? = null,
    val businessDescription: String? = null,


    // google map url
    val businessLocationUrl: String? = null,

    // location
    val businessAddressText: String? = null,
    val businessLatitude: Double? = null,
    val businessLongitude: Double? = null,

    // media
    val businessStoreFrontImageUrl: String? = null,

    // default verification = UNVERIFIED
    val businessLicenseVerified: VerificationStatus = VerificationStatus.UNVERIFIED,
    val businessEmailVerified: VerificationStatus = VerificationStatus.UNVERIFIED,
    val businessPhoneVerified: VerificationStatus = VerificationStatus.UNVERIFIED,

    // admin decide the status when review form send form employer, default = UNVERIFIED
    val submissionStatus: VerificationStatus = VerificationStatus.UNVERIFIED,
    val rejectionReason: String? = null,

    // system log
    val submittedAt: Long = 0L,
    val reviewedAt: Long? = null,
    val reviewedBy: String? = null,
)
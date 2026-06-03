package com.studentjobs.app.feature.profile.employer

import android.net.Uri
import com.studentjobs.app.data.model.status.VerificationStatus

data class EmployerVerificationUiState(

    // ===== BUSINESS INFO =====

    val businessName: String = "",

    val businessCategory: String = "",

    val businessAddressText: String = "",

    val businessLocationUrl: String = "",

    val businessDescription: String = "",

    // ===== MEDIA =====

    val businessLicenseUri: Uri? = null,

    val businessStoreFrontUri: Uri? = null,

    // ===== VERIFY =====

    val businessEmailVerified:
    VerificationStatus =
        VerificationStatus.UNVERIFIED,

    val businessPhoneVerified:
    VerificationStatus =
        VerificationStatus.UNVERIFIED,

    val businessLicenseVerified:
    VerificationStatus =
        VerificationStatus.UNVERIFIED,

    val submissionStatus:
    VerificationStatus =
        VerificationStatus.UNVERIFIED,

    val rejectionReason: String? = null,

    // ===== SYSTEM =====

    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    val verificationSubmitted: Boolean = false,

    // ===== LOCATION =====

    val businessLatitude: Double? = null,

    val businessLongitude: Double? = null,

    val addressVerified: Boolean = false,
)
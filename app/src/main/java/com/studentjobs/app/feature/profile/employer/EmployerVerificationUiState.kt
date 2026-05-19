package com.studentjobs.app.feature.profile.employer

import android.net.Uri

data class EmployerVerificationUiState(
    val businessName: String = "",
    val businessAddress: String = "",
    val businessCategory: String = "",
    val googleMapsUrl: String = "",
    val businessDescription: String = " ",

    val businessLicenseUri: Uri? = null,
    val storeFrontUri: Uri? = null,

    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,

    val verificationStatus: String = "UNVERIFIED",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    //
    val verificationSubmitted: Boolean = false
)
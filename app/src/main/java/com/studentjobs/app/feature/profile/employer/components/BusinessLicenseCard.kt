package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.runtime.Composable
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun BusinessLicenseCard(
    state: ProfileUiState
) {
    ExpandableImageCard(
        title = "Giấy phép kinh doanh",
        imageUrl = state.employerVerification?.businessLicenseUrl,
        icon = Icons.Default.Badge
    )
}
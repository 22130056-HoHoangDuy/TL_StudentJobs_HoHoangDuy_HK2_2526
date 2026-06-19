package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.runtime.Composable
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun StorefrontCard(
    state: ProfileUiState
) {
    ExpandableImageCard(
        title = "Ảnh cửa hàng (Storefront)",
        imageUrl = state.employerVerification?.businessStoreFrontImageUrl,
        icon = Icons.Default.Storefront
    )
}
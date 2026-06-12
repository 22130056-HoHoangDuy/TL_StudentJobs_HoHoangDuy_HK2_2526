package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.feature.profile.ProfileUiState
import com.studentjobs.app.feature.profile.shared.components.PlusBannerCard
import com.studentjobs.app.feature.profile.shared.components.TrustScoreCard

@Composable
fun VerifiedEmployerProfile(
    state: ProfileUiState,
    onEditSection: (String) -> Unit,
    onUpgradePlusClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSelectLocation: () -> Unit // Nhận callback điều hướng map
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BusinessHeaderCard(
                state = state,
                onEditHeader = { onEditSection("header") }
            )
        }

        item {
            PlusBannerCard(
                currentPlan = state.userCore?.subscriptionPlan ?: SubscriptionPlan.FREE,
                role = state.role,
                onUpgradePlusClick = onUpgradePlusClick
            )
        }

        item { RecruitmentStatsCard(state) }

        item { TrustScoreCard(state) }

        item {
            BusinessInfoCard(
                state = state,
                onEditClick = { onEditSection("info") }
            )
        }

        item {
            EmployerContactCard(
                state = state,
                onEditClick = { onEditSection("contact") },
                onSelectLocation = onSelectLocation // Đưa callback vào đây
            )
        }

        item { StorefrontCard(state = state) }

        item { BusinessLicenseCard(state = state) }

        item {
            TextButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
            ) {
                Text("Đăng xuất tài khoản", style = MaterialTheme.typography.bodyMedium)
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
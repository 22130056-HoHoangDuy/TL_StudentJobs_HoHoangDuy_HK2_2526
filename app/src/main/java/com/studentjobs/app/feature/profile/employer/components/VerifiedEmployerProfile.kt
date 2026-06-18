package com.studentjobs.app.feature.profile.employer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.feature.profile.ProfileUiState
import com.studentjobs.app.feature.profile.employer.components.BusinessHeaderCard
import com.studentjobs.app.feature.profile.employer.components.BusinessInfoComponent
import com.studentjobs.app.feature.profile.employer.components.BusinessLicenseCard
import com.studentjobs.app.feature.profile.employer.components.RecruitmentStatsCard
import com.studentjobs.app.feature.profile.employer.components.StorefrontCard
import com.studentjobs.app.feature.profile.shared.components.PlusBannerCard
import com.studentjobs.app.feature.profile.shared.components.TrustScoreCard

@Composable
fun VerifiedEmployerProfile(
    state: ProfileUiState,
    onEditSection: (String) -> Unit,
    onUpgradePlusClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Luôn có một giá trị fallback trống nếu profile null để tránh crash
    val profile = state.employerProfile

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item { BusinessHeaderCard(state, { onEditSection("header") }, onSettingsClick) }

        // Stats & Trust
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlusBannerCard(
                    state.userCore?.subscriptionPlan ?: SubscriptionPlan.FREE,
                    state.role,
                    onUpgradePlusClick
                )
                RecruitmentStatsCard(state)
                TrustScoreCard(state)
            }
        }

        // Info Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                BusinessInfoComponent(
                    isEditable = false,
                    // Sử dụng toán tử Elvis để đảm bảo luôn truyền String rỗng thay vì null
                    businessName = profile?.businessName ?: "Chưa cập nhật tên",
                    businessCategory = profile?.businessCategory ?: "Chưa chọn ngành nghề",
                    businessAddress = profile?.businessAddressText ?: "Chưa cập nhật địa chỉ",
                    businessDesc = profile?.businessDescription ?: "Doanh nghiệp chưa có mô tả",
                    businessUrl = profile?.businessLocationUrl ?: "",
                    onEditClick = { onEditSection("info") }
                )
            }
        }

        // Media
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StorefrontCard(state)
                BusinessLicenseCard(state)
            }
        }

        // Footer
        item {
            TextButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
            ) {
                Text("Đăng xuất tài khoản", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
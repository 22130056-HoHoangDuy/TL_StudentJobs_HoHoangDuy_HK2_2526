package com.studentjobs.app.feature.profile.student.components

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
import com.studentjobs.app.feature.profile.shared.components.ContactInfoCard
import com.studentjobs.app.feature.profile.shared.components.GradientHeader
import com.studentjobs.app.feature.profile.shared.components.PlusBannerCard
import com.studentjobs.app.feature.profile.shared.components.TrustScoreCard
import com.studentjobs.app.feature.profile.shared.components.VerificationStatusCard

@Composable
fun VerifiedStudentProfile(
    state: ProfileUiState,
    onUpgradePlusClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onSelectLocation: () -> Unit,
    onManageSkills: () -> Unit,
    onLogoutClick: () -> Unit // Bổ sung callback logout
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Tự tạo khoảng cách, không cần lạm dụng Spacer
    ) {

        item { GradientHeader(state = state) }

        item {
            PlusBannerCard(
                currentPlan = state.userCore?.subscriptionPlan ?: SubscriptionPlan.FREE,
                role = state.role,
                onUpgradePlusClick = onUpgradePlusClick
            )
        }
//
        item { ScheduleFeatureCard(onClick = onScheduleClick) }

        item { TrustScoreCard(state = state) }

        item { VerificationStatusCard(state = state) }

        item { AcademicInfoCard(state = state) }

        item { ContactInfoCard(state = state) }

        item { LocationInfoCard(state = state, onSelectLocation = onSelectLocation) }

        item { SkillsCard(state = state, onManageSkills = onManageSkills) }
//
        // Nút Đăng xuất đồng bộ ở đáy danh sách Sinh viên
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

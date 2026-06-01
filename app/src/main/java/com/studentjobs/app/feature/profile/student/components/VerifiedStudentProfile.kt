package com.studentjobs.app.feature.profile.student.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.feature.profile.ProfileUiState
import com.studentjobs.app.feature.profile.shared.components.ContactInfoCard
import com.studentjobs.app.feature.profile.shared.components.GradientHeader
import com.studentjobs.app.feature.profile.shared.components.PlusBannerCard
import com.studentjobs.app.feature.profile.shared.components.TrustScoreCard
import com.studentjobs.app.feature.profile.shared.components.VerificationStatusCard
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@Composable
fun VerifiedStudentProfile(

    state: ProfileUiState,

    onUpgradePlusClick: () -> Unit,

    onScheduleClick: () -> Unit,

    onSelectLocation: () -> Unit

) {

    Column {

        // ========================================
        // HEADER
        // ========================================

        GradientHeader(
            state = state
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // ========================================
        // PLUS
        // ========================================

        PlusBannerCard(

            currentPlan =
                state.userCore?.subscriptionPlan
                    ?: SubscriptionPlan.FREE,

            role = state.role,

            onUpgradePlusClick = {

                onUpgradePlusClick()
            }
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // ========================================
        // TIMETABLE OCR
        // ========================================

        ScheduleFeatureCard(

            onClick = {

                onScheduleClick()
            }
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // ========================================
        // TRUST SCORE
        // ========================================

        TrustScoreCard(
            state = state
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        // ========================================
        // VERIFICATION
        // ========================================

        VerificationStatusCard(
            state = state
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        // ========================================
        // ACADEMIC
        // ========================================

        AcademicInfoCard(
            state = state
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        // ========================================
        // CONTACT
        // ========================================

        ContactInfoCard(
            state = state
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        LocationInfoCard(

            state = state,

            onSelectLocation = {

                onSelectLocation()
            }
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        // ========================================
        // SKILLS
        // ========================================

        SkillsCard(
            state = state
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )
    }
}
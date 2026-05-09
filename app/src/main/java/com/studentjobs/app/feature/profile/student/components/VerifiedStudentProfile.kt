package com.studentjobs.app.feature.profile.student.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.ProfileUiState
import com.studentjobs.app.feature.profile.shared.components.ContactInfoCard
import com.studentjobs.app.feature.profile.shared.components.GradientHeader
import com.studentjobs.app.feature.profile.shared.components.TrustScoreCard
import com.studentjobs.app.feature.profile.shared.components.VerificationStatusCard

@Composable
fun VerifiedStudentProfile(
    state: ProfileUiState
) {

    Column {

        GradientHeader(state)

        Spacer(modifier = Modifier.height(20.dp))

        TrustScoreCard(state)

        Spacer(modifier = Modifier.height(16.dp))

        VerificationStatusCard(state)

        Spacer(modifier = Modifier.height(16.dp))

        AcademicInfoCard(state)

        Spacer(modifier = Modifier.height(16.dp))

        ContactInfoCard(state)

        Spacer(modifier = Modifier.height(16.dp))

        SkillsCard(state)

        Spacer(modifier = Modifier.height(16.dp))

    }
}
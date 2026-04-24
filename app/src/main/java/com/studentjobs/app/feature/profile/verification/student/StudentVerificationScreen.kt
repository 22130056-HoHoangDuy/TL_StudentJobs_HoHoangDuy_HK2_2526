package com.studentjobs.app.feature.profile.verification.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.components.VerificationCard
import com.studentjobs.app.feature.profile.components.VerificationStatus

@Composable
fun StudentVerificationScreen() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        VerificationCard(
            title = "Verify Email",
            description = "Confirm your email address",
            status = VerificationStatus.NOT_VERIFIED,
            onClick = { /* TODO */ }
        )

        VerificationCard(
            title = "Verify Phone",
            description = "Confirm your phone number",
            status = VerificationStatus.NOT_VERIFIED,
            onClick = { /* TODO */ }
        )

        VerificationCard(
            title = "Student Card",
            description = "Upload your student ID",
            status = VerificationStatus.NOT_VERIFIED,
            onClick = { /* TODO */ }
        )
    }
}
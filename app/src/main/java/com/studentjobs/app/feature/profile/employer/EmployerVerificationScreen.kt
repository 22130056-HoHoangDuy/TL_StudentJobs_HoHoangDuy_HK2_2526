package com.studentjobs.app.feature.profile.verification.employer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.shared.components.VerificationCard
import com.studentjobs.app.feature.profile.shared.components.VerificationStatus

@Composable
fun EmployerVerificationScreen() {

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
            title = "Business License",
            description = "Verify your company",
            status = VerificationStatus.PENDING,
            onClick = { /* TODO */ }
        )

        VerificationCard(
            title = "Company Info",
            description = "Complete company details",
            status = VerificationStatus.NOT_VERIFIED,
            onClick = { /* TODO */ }
        )
    }
}
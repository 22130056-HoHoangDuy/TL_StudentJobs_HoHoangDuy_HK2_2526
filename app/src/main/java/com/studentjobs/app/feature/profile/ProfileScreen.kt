package com.studentjobs.app.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.feature.profile.components.ProfileHeader
import com.studentjobs.app.feature.profile.components.TrustScoreSection
import com.studentjobs.app.feature.profile.components.VerificationBanner
import com.studentjobs.app.feature.profile.verification.employer.EmployerVerificationScreen
import com.studentjobs.app.feature.profile.verification.student.StudentVerificationScreen

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val isVerified = state.trustScore >= 80

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        ProfileHeader(
            name = state.name,
            email = state.email
        )

        Spacer(modifier = Modifier.height(16.dp))

        TrustScoreSection(state.trustScore)

        Spacer(modifier = Modifier.height(16.dp))

        if (!isVerified) {
            VerificationBanner()
            Spacer(modifier = Modifier.height(16.dp))
        }

        when (state.role) {
            UserRole.STUDENT -> StudentVerificationScreen()
            UserRole.EMPLOYER -> EmployerVerificationScreen()
        }
    }
}
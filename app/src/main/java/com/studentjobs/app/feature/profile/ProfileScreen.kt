package com.studentjobs.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.feature.profile.components.ProfileCompletionSection
import com.studentjobs.app.feature.profile.components.ProfileHeader

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()

    // 🔄 Loading
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 📱 UI chính
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // 🔥 HEADER
        ProfileHeader(
            name = state.name,
            email = state.email
        )

        Spacer(modifier = Modifier.height(20.dp))

        // PROFILE COMPLETION
        ProfileCompletionSection(
            isStudentVerified = state.isStudentVerified,
            isPhoneVerified = state.isPhoneVerified,
            isEmailVerified = state.isEmailVerified,

            // Student verification
            onStudentClick = {
                if (!state.isStudentVerified) {
                    navController.navigate("student_verification") {
                        launchSingleTop = true
                    }
                }
            },

            // Phone verification
            onPhoneClick = {
                if (!state.isPhoneVerified) {
                    navController.navigate("phone_verification")
                }
            },

            // Email verification
            onEmailClick = {
                if (!state.isEmailVerified) {
                    navController.navigate("email_verification") {
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}
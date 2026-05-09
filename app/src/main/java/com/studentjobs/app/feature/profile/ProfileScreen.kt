package com.studentjobs.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.feature.profile.student.components.ProfileCompletionSection
import com.studentjobs.app.feature.profile.student.components.VerifiedStudentProfile

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()

    // Loading
    if (state.isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    // Dynamic email verification state
    val emailVerified =
        if (state.role == UserRole.STUDENT)
            state.isStudentEmailVerified
        else
            state.isEmailVerified

    // FULL VERIFIED
    val fullyVerified =
        state.isStudentVerified &&
                state.isPhoneVerified &&
                state.isStudentEmailVerified

    // Main UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // VERIFIED PROFILE
        if (
            state.role == UserRole.STUDENT &&
            fullyVerified
        ) {

            VerifiedStudentProfile(
                state = state
            )

        } else {

            // PROFILE COMPLETION
            ProfileCompletionSection(

                isStudentVerified = state.isStudentVerified,

                isPhoneVerified = state.isPhoneVerified,

                isEmailVerified = emailVerified,

                isStudentEmailVerified = state.isStudentEmailVerified,

                // Student verification
                onStudentClick = {

                    if (!state.isStudentVerified) {

                        navController.navigate(
                            "student_verification"
                        ) {
                            launchSingleTop = true
                        }
                    }
                },

                // Phone verification
                onPhoneClick = {

                    if (!state.isPhoneVerified) {

                        navController.navigate(
                            "phone_verification"
                        )
                    }
                },

                // Email verification
                onEmailClick = {

                    if (!emailVerified) {

                        navController.navigate(
                            "email_verification/${state.role.name}"
                        )
                    }
                }
            )
        }
    }
}
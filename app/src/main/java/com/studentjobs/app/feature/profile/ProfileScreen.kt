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
import com.studentjobs.app.feature.profile.employer.EmployerVerificationScreen
import com.studentjobs.app.feature.profile.employer.components.VerifiedEmployerProfile
import com.studentjobs.app.feature.profile.student.components.ProfileCompletionSection
import com.studentjobs.app.feature.profile.student.components.VerifiedStudentProfile

@Composable
fun ProfileScreen(
    navController: NavController, viewModel: ProfileViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()

    // Loading
    if (state.isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        when (state.role) {
            // STUDENT FLOW
            UserRole.STUDENT -> {

                val fullyVerified =
                    state.isStudentVerified && state.isPhoneVerified && state.isStudentEmailVerified

                if (fullyVerified) {

                    VerifiedStudentProfile(
                        state = state
                    )

                } else {

                    ProfileCompletionSection(

                        isStudentVerified = state.isStudentVerified,

                        isPhoneVerified = state.isPhoneVerified,

                        isEmailVerified = state.isStudentEmailVerified,

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

                            if (!state.isStudentEmailVerified) {

                                navController.navigate(
                                    "email_verification/STUDENT"
                                )
                            }
                        })
                }
            }
            // EMPLOYER FLOW
            UserRole.EMPLOYER -> {

                if (state.isBusinessVerified) {

                    VerifiedEmployerProfile(
                        state = state
                    )

                } else {
                    EmployerVerificationScreen(navController)
                }
            }
        }
    }
}
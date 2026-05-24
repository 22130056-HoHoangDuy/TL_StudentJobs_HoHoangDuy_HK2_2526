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
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.feature.profile.employer.EmployerVerificationScreen
import com.studentjobs.app.feature.profile.employer.components.VerifiedEmployerProfile
import com.studentjobs.app.feature.profile.student.components.ProfileCompletionSection
import com.studentjobs.app.feature.profile.student.components.VerifiedStudentProfile

@Composable
fun ProfileScreen(

    navController: NavController,

    viewModel: ProfileViewModel = viewModel()

) {

    val state by viewModel.uiState.collectAsState()

    // ========================================
    // LOADING
    // ========================================

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
            .background(
                MaterialTheme.colorScheme.background
            )
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {

        when (state.role) {

            // ========================================
            // STUDENT FLOW
            // ========================================

            UserRole.STUDENT -> {

                val verification = state.studentVerification

                val fullyVerified =

                    verification?.studentCardVerified ==

                            VerificationStatus.VERIFIED

                            &&

                            verification.studentPhoneVerified ==

                            VerificationStatus.VERIFIED

                            &&

                            verification.studentEmailVerified ==

                            VerificationStatus.VERIFIED

                if (fullyVerified) {

                    VerifiedStudentProfile(
                        state = state,
                        onUpgradePlusClick = {

                            navController.navigate(
                                "subscription"
                            )
                        }
                    )

                } else {

                    ProfileCompletionSection(

                        // ========================================
                        // VERIFY STATE
                        // ========================================

                        isStudentVerified =

                            verification?.studentCardVerified ==

                                    VerificationStatus.VERIFIED,

                        isPhoneVerified =

                            verification?.studentPhoneVerified ==

                                    VerificationStatus.VERIFIED,

                        isEmailVerified =

                            verification?.studentEmailVerified ==

                                    VerificationStatus.VERIFIED,

                        isStudentEmailVerified =

                            verification?.studentEmailVerified ==

                                    VerificationStatus.VERIFIED,

                        // ========================================
                        // STUDENT VERIFY
                        // ========================================

                        onStudentClick = {

                            if (

                                verification?.studentCardVerified !=

                                VerificationStatus.VERIFIED

                            ) {

                                navController.navigate(
                                    "student_verification"
                                ) {

                                    launchSingleTop = true
                                }
                            }
                        },

                        // ========================================
                        // PHONE VERIFY
                        // ========================================

                        onPhoneClick = {

                            if (

                                verification?.studentPhoneVerified !=

                                VerificationStatus.VERIFIED

                            ) {

                                navController.navigate(
                                    "phone_verification/STUDENT"
                                )
                            }
                        },

                        // ========================================
                        // EMAIL VERIFY
                        // ========================================

                        onEmailClick = {

                            if (

                                verification?.studentEmailVerified !=

                                VerificationStatus.VERIFIED

                            ) {

                                navController.navigate(
                                    "email_verification/STUDENT"
                                )
                            }
                        })
                }
            }

            // ========================================
            // EMPLOYER FLOW
            // ========================================

            UserRole.EMPLOYER -> {

                val verification = state.employerVerification

                val fullyVerified =

                    verification?.submissionStatus ==

                            VerificationStatus.VERIFIED

                if (fullyVerified) {

                    VerifiedEmployerProfile(
                        state = state,
                        onUpgradePlusClick = {

                            navController.navigate(
                                "subscription"
                            )
                        }
                    )

                } else {

                    EmployerVerificationScreen(
                        navController
                    )
                }
            }
        }
    }
}
package com.studentjobs.app.feature.profile.verification.email

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.feature.profile.verification.email.employer.EmployerEmailVerificationScreen
import com.studentjobs.app.feature.profile.verification.email.student.StudentEmailVerificationScreen

@Composable
fun EmailVerificationScreen(
    role: UserRole, navController: NavController
) {

    when (role) {

        UserRole.STUDENT -> {
            StudentEmailVerificationScreen(navController)
        }

        UserRole.EMPLOYER -> {
            EmployerEmailVerificationScreen(navController)
        }
    }
}
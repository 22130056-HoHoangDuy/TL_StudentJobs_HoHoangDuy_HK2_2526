package com.studentjobs.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.feature.home.HomeEntryScreen
import com.studentjobs.app.feature.profile.ProfileScreen
import com.studentjobs.app.feature.profile.student.StudentVerificationScreen
import com.studentjobs.app.feature.profile.verification.email.EmailVerificationScreen
import com.studentjobs.app.feature.profile.verification.phone.employer.EmployerPhoneVerificationScreen
import com.studentjobs.app.feature.profile.verification.phone.student.StudentPhoneVerificationScreen

@Composable
fun MainNavGraph(

    navController: NavHostController,

    modifier: Modifier = Modifier

) {

    NavHost(

        navController = navController,

        startDestination = "home",

        modifier = modifier

    ) {

        // ========================================
        // HOME
        // ========================================

        composable("home") {

            HomeEntryScreen()
        }

        // ========================================
        // PROFILE
        // ========================================

        composable("profile") {

            ProfileScreen(navController)
        }

        // ========================================
        // JOBS
        // ========================================

        composable("jobs") {

            Text("Jobs")
        }

        // ========================================
        // MESSAGES
        // ========================================

        composable("messages") {

            Text("Messages")
        }

        // ========================================
        // STUDENT VERIFICATION
        // ========================================

        composable(
            "student_verification"
        ) {

            StudentVerificationScreen(
                navController
            )
        }

        // ========================================
        // STUDENT PHONE VERIFICATION
        // ========================================

        composable(
            "phone_verification/STUDENT"
        ) {

            StudentPhoneVerificationScreen(
                navController
            )
        }

        // ========================================
        // EMPLOYER PHONE VERIFICATION
        // ========================================

        composable(
            "phone_verification/EMPLOYER"
        ) {

            EmployerPhoneVerificationScreen(
                navController
            )
        }

        // ========================================
        // EMAIL VERIFICATION
        // ========================================

        composable(
            "email_verification/{role}"
        ) { backStackEntry ->

            val role = UserRole.valueOf(

                backStackEntry
                    .arguments
                    ?.getString("role")

                    ?: "STUDENT"
            )

            EmailVerificationScreen(

                role = role,

                navController = navController
            )
        }
    }
}
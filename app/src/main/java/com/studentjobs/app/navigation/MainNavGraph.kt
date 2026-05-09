package com.studentjobs.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.feature.home.HomeEntryScreen
import com.studentjobs.app.feature.profile.ProfileScreen
import com.studentjobs.app.feature.profile.verification.email.EmailVerificationScreen
import com.studentjobs.app.feature.profile.verification.phone.PhoneVerificationScreen
import com.studentjobs.app.feature.profile.verification.student.StudentVerificationScreen

@Composable
fun MainNavGraph(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, startDestination = "home", modifier = modifier
    ) {

        composable("home") {
            HomeEntryScreen()
        }

        composable("profile") {
            ProfileScreen(navController)
        }

        composable("jobs") {
            Text("Jobs")
        }

        composable("messages") {
            Text("Messages")
        }

        // 🔥 ADD
        composable("student_verification") {
            StudentVerificationScreen(navController)
        }

        composable("phone_verification") {
            PhoneVerificationScreen(navController)
        }

        composable("email_verification/{role}") { backStackEntry ->

            val role = UserRole.valueOf(
                backStackEntry.arguments?.getString("role") ?: "STUDENT"
            )
            EmailVerificationScreen(
                role = role, navController = navController
            )
        }
    }
}
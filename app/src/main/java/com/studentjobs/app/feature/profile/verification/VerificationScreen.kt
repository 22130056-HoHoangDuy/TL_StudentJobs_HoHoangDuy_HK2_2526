package com.studentjobs.app.feature.profile.verification

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.feature.profile.verification.employer.EmployerVerificationScreen
import com.studentjobs.app.feature.profile.verification.student.StudentVerificationScreen
import com.studentjobs.app.utils.AppPreferences

@Composable
fun VerificationScreen(
    role: String,
    navController: NavController
) {

    val context = LocalContext.current

    val role = remember {
        AppPreferences(context).getUserRole()
    }

    when (role) {
        UserRole.STUDENT.name -> StudentVerificationScreen(navController)
        UserRole.EMPLOYER.name -> EmployerVerificationScreen()
        else -> Text("Unknown role")
    }
}
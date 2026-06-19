package com.studentjobs.app.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.studentjobs.app.feature.home.employer.EmployerHomeScreen
import com.studentjobs.app.feature.home.student.StudentHomeScreen
import com.studentjobs.app.utils.AppPreferences

@Composable
fun HomeEntryScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val role = AppPreferences(context).getUserRole()

    when (role) {
        "STUDENT" -> StudentHomeScreen(navController)
        "EMPLOYER" -> EmployerHomeScreen(navController)
    }
}
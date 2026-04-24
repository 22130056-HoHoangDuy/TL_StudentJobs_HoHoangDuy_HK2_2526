package com.studentjobs.app.feature.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.studentjobs.app.feature.home.student.StudentHomeScreen
import com.studentjobs.app.utils.AppPreferences

@Composable
fun HomeEntryScreen() {

    val context = LocalContext.current
    val role = AppPreferences(context).getUserRole()

    when (role) {
        "STUDENT" -> StudentHomeScreen()
        "EMPLOYER" -> Text("Employer Home")
    }
}
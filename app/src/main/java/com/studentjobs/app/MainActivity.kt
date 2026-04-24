package com.studentjobs.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.studentjobs.app.ui.theme.StudentJobsTheme
import com.studentjobs.app.navigation.AppNavGraph
import com.studentjobs.app.viewmodel.AuthViewModel
import com.studentjobs.app.data.repository.AuthRepository
import com.studentjobs.app.firebase.auth.AuthService
import com.studentjobs.app.firebase.firestore.UserService

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authService = AuthService()
        val userService = UserService()
        val repository = AuthRepository(authService, userService)
        val viewModel = AuthViewModel(repository)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            StudentJobsTheme {
                AppNavGraph(viewModel)
            }
        }
    }
}
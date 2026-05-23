package com.studentjobs.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.studentjobs.app.data.repository.auth.AuthRepository
import com.studentjobs.app.feature.auth.AuthViewModel
import com.studentjobs.app.firebase.auth.AuthService
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.StudentService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import com.studentjobs.app.firebase.firestore.VerificationService
import com.studentjobs.app.navigation.AppNavGraph
import com.studentjobs.app.ui.theme.StudentJobsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // ====================================
        // SERVICES
        // ====================================
        val authService =
            AuthService()

        val userService =
            UserServiceNew()

        val studentService =
            StudentService()

        val employerService =
            EmployerService()

        val verificationService =
            VerificationService()

        // ====================================
        // REPOSITORY
        // ====================================
        val repository =
            AuthRepository(

                authService =
                    authService,

                userService =
                    userService,

                studentService =
                    studentService,

                employerService =
                    employerService,

                verificationService =
                    verificationService
            )

        // ====================================
        // VIEWMODEL
        // ====================================
        val viewModel =
            AuthViewModel(repository)

        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        setContent {

            StudentJobsTheme {

                AppNavGraph(viewModel)
            }
        }
    }
}
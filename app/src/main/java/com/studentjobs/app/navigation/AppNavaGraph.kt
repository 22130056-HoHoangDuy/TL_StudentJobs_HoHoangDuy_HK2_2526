package com.studentjobs.app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.feature.auth.AuthViewModel
import com.studentjobs.app.feature.auth.LoginScreen
import com.studentjobs.app.feature.auth.RegisterScreen
import com.studentjobs.app.feature.auth.forgot.ForgotPasswordScreen
import com.studentjobs.app.feature.onboarding.OnBoardingScreen
import com.studentjobs.app.feature.role.RoleSelectionScreen
import com.studentjobs.app.ui.MainScreen
import com.studentjobs.app.utils.AppPreferences

@Composable
fun AppNavGraph(viewModel: AuthViewModel) {

    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = AppPreferences(context)

    val DEBUG_ALWAYS_SHOW_ONBOARDING = true

    val currentUser =

        FirebaseAuth
            .getInstance()
            .currentUser

    val startDestination = when {

        currentUser != null -> "main"

        prefs.isOnboardingShown() -> "login"

        else -> "onboarding"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ONBOARDING
        composable("onboarding") {
            OnBoardingScreen(
                onFinish = {
                    prefs.setOnboardingShown()
                    navController.navigate("role") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        // ROLE
        composable("role") {
            RoleSelectionScreen(
                onContinue = { role ->
                    prefs.saveUserRole(role.name)

                    navController.navigate("login") {
                        popUpTo("role") { inclusive = true }
                    }
                }
            )
        }

        // LOGIN
        composable("login") {
            LoginScreen(
                viewModel = viewModel,

                onNavigateToRegister = {
                    navController.navigate("register")
                },

                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
                },

                onLoginSuccess = { user ->

                    prefs.saveUserRole(
                        user.role.name
                    )

                    navController.navigate("main") {

                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // REGISTER (🔥 FIX QUAN TRỌNG)
        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    // 👉 Sau khi register → quay về login
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("forgot_password") {

            ForgotPasswordScreen(

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("main") {

            MainScreen(

                onLogout = {

                    viewModel.logout()

                    navController.navigate("login") {

                        popUpTo("main") {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }

                    Log.d(
                        "LOGOUT",
                        "AFTER NAVIGATE"
                    )
                }
            )
        }
    }
}
package com.studentjobs.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studentjobs.app.ui.screen.auth.LoginScreen
import com.studentjobs.app.ui.screen.auth.RegisterScreen
import com.studentjobs.app.ui.screen.onboarding.OnBoardingScreen
import com.studentjobs.app.ui.screen.role.RoleSelectionScreen
import com.studentjobs.app.utils.AppPreferences
import com.studentjobs.app.viewmodel.AuthViewModel

@Composable
fun AppNavGraph(viewModel: AuthViewModel) {

    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = AppPreferences(context)

    // 👉 DEBUG MODE
    val DEBUG_ALWAYS_SHOW_ONBOARDING = true

    val startDestination = when {
        DEBUG_ALWAYS_SHOW_ONBOARDING -> "onboarding"
        prefs.isOnboardingShown() -> "login"
        else -> "onboarding"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ========================
        // ONBOARDING
        // ========================
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

        // ========================
        // ROLE
        // ========================
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

        // ========================
        // LOGIN
        // ========================
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // ========================
        // REGISTER
        // ========================
        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // ========================
        // HOME
        // ========================
        composable("home") {
            Text("Home Screen")
        }
    }
}
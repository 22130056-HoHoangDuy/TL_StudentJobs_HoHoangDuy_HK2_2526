package com.studentjobs.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
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
import com.studentjobs.app.feature.splash.SplashScreen
import com.studentjobs.app.session.UserSession
import com.studentjobs.app.ui.MainScreen
import com.studentjobs.app.utils.AppPreferences

@Composable
fun AppNavGraph(
    viewModel: AuthViewModel
) {

    val navController = rememberNavController()

    val context = LocalContext.current

    val prefs = AppPreferences(context)

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // ========================================
        // SPLASH
        // ========================================

        composable("splash") {

            SplashScreen(

                onNavigateLogin = {

                    navController.navigate("login") {

                        popUpTo("splash") {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },

                onNavigateMain = { showDialog ->

                    UserSession.shouldShowVerificationDialog =
                        showDialog

                    navController.navigate("main") {

                        popUpTo("splash") {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // ========================================
        // ONBOARDING
        // ========================================

        composable("onboarding") {

            OnBoardingScreen(

                onFinish = {

                    prefs.setOnboardingShown()

                    navController.navigate("role") {

                        popUpTo("onboarding") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ========================================
        // ROLE
        // ========================================

        composable("role") {

            RoleSelectionScreen(

                onContinue = { role ->

                    prefs.saveUserRole(
                        role.name
                    )

                    navController.navigate("login") {

                        popUpTo("role") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ========================================
        // LOGIN
        // ========================================

        composable("login") {

            LoginScreen(

                viewModel = viewModel,

                onNavigateToRegister = {

                    navController.navigate(
                        "register"
                    )
                },

                onForgotPasswordClick = {

                    navController.navigate(
                        "forgot_password"
                    )
                },

                onLoginSuccess = { user ->

                    prefs.saveUserRole(
                        user.role.name
                    )

                    navController.navigate("main") {

                        popUpTo("login") {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // ========================================
        // REGISTER
        // ========================================

        composable("register") {

            RegisterScreen(

                viewModel = viewModel,

                onRegisterSuccess = {

                    navController.navigate("login") {

                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ========================================
        // FORGOT PASSWORD
        // ========================================

        composable("forgot_password") {

            ForgotPasswordScreen(

                onBackClick = {

                    navController.popBackStack()
                }
            )
        }

        // ========================================
        // MAIN
        // ========================================

        composable("main") {

            val currentUid =
                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid
                    ?: "guest"

            key(currentUid) {

                MainScreen(

                    onLogout = {

                        // reset session flags
                        UserSession.shouldShowVerificationDialog = false

                        // firebase sign out
                        viewModel.logout()

                        // clear entire nav stack
                        navController.navigate("login") {

                            popUpTo(
                                navController.graph.id
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
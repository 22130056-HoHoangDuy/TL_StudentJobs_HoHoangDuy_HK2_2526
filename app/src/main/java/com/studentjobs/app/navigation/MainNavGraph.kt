package com.studentjobs.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.feature.home.HomeEntryScreen
import com.studentjobs.app.feature.job.create.CreateJobScreen
import com.studentjobs.app.feature.job.create.CreateJobViewModel
import com.studentjobs.app.feature.job.create.CreateJobViewModelFactory
import com.studentjobs.app.feature.profile.ProfileScreen
import com.studentjobs.app.feature.profile.student.StudentVerificationScreen
import com.studentjobs.app.feature.profile.verification.email.EmailVerificationScreen
import com.studentjobs.app.feature.profile.verification.phone.employer.EmployerPhoneVerificationScreen
import com.studentjobs.app.feature.profile.verification.phone.student.StudentPhoneVerificationScreen
import com.studentjobs.app.feature.schedule.ScheduleScreen
import com.studentjobs.app.feature.schedule.ScheduleUploadScreen
import com.studentjobs.app.feature.subscription.SubscriptionRequestScreen
import com.studentjobs.app.feature.subscription.SubscriptionScreen
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.JobService
import com.studentjobs.app.firebase.firestore.ShiftService

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

            HomeEntryScreen(navController)
        }

        // ========================================
        // PROFILE
        // ========================================

        composable("profile") {

            ProfileScreen(navController)
        }

        // ========================================
        // SUBSCRIPTION
        // ========================================

        composable(
            "subscription/{role}"
        ) { backStackEntry ->

            val role = UserRole.valueOf(

                backStackEntry
                    .arguments
                    ?.getString("role")

                    ?: "STUDENT"
            )

            SubscriptionScreen(

                role = role,

                currentPlan =
                    SubscriptionPlan.FREE,

                onUpgradePlusClick = {

                    navController.navigate(

                        "subscription_request/${role.name}"
                    )
                },

                onBackClick = {

                    navController.popBackStack()
                }
            )
        }

        // ========================================
        // SUBSCRIPTION REQUEST
        // ========================================

        composable(
            "subscription_request/{role}"
        ) { backStackEntry ->

            val role = UserRole.valueOf(

                backStackEntry
                    .arguments
                    ?.getString("role")

                    ?: "STUDENT"
            )

            SubscriptionRequestScreen(

                role = role,

                onBackClick = {

                    navController.popBackStack()
                },

                onRequestSuccess = {

                    navController.popBackStack()
                }
            )
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
        composable("schedule") {

            ScheduleScreen(
                navController = navController,
                currentPlan = SubscriptionPlan.PLUS
            )
        }

        composable("schedule_upload") {

            ScheduleUploadScreen()
        }
        composable("create_job") {

            val repository =

                JobRepository(

                    jobService =
                        JobService(),

                    shiftService =
                        ShiftService(),

                    employerService =
                        EmployerService()
                )

            val factory =

                CreateJobViewModelFactory(
                    repository
                )

            val viewModel:
                    CreateJobViewModel =

                viewModel(
                    factory = factory
                )

            CreateJobScreen(

                employerBusinessName = "",

                viewModel = viewModel,

                onNavigateToSubscription = {

                    navController.navigate(
                        "subscription/EMPLOYER"
                    )
                }
            )
        }
    }
}
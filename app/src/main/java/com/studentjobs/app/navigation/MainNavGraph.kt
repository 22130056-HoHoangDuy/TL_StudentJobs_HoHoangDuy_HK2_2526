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
import com.studentjobs.app.data.repository.application.ApplicationRepository
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.notification.NotificationRepository
import com.studentjobs.app.data.repository.student.StudentRepository
import com.studentjobs.app.data.repository.user.UserRepository
import com.studentjobs.app.feature.application.apply.ApplyJobViewModel
import com.studentjobs.app.feature.application.apply.ApplyJobViewModelFactory
import com.studentjobs.app.feature.application.employer.ActiveJobScreen
import com.studentjobs.app.feature.application.employer.ActiveJobViewModel
import com.studentjobs.app.feature.application.employer.ActiveJobViewModelFactory
import com.studentjobs.app.feature.application.employer.ApplicantListScreen
import com.studentjobs.app.feature.application.employer.ApplicantListViewModel
import com.studentjobs.app.feature.application.employer.ApplicantListViewModelFactory
import com.studentjobs.app.feature.application.student.MyApplicationsScreen
import com.studentjobs.app.feature.application.student.MyApplicationsViewModel
import com.studentjobs.app.feature.job.JobEntryScreen
import com.studentjobs.app.feature.job.create.CreateJobScreen
import com.studentjobs.app.feature.job.create.CreateJobViewModel
import com.studentjobs.app.feature.job.create.CreateJobViewModelFactory
import com.studentjobs.app.feature.job.detail.JobDetailScreen
import com.studentjobs.app.feature.job.detail.JobDetailViewModel
import com.studentjobs.app.feature.job.detail.JobDetailViewModelFactory
import com.studentjobs.app.feature.location.LocationPickerScreen
import com.studentjobs.app.feature.profile.ProfileScreen
import com.studentjobs.app.feature.profile.student.StudentVerificationScreen
import com.studentjobs.app.feature.profile.verification.email.EmailVerificationScreen
import com.studentjobs.app.feature.profile.verification.phone.employer.EmployerPhoneVerificationScreen
import com.studentjobs.app.feature.profile.verification.phone.student.StudentPhoneVerificationScreen
import com.studentjobs.app.feature.schedule.ScheduleScreen
import com.studentjobs.app.feature.schedule.ScheduleUploadScreen
import com.studentjobs.app.feature.skill.ManageSkillsScreen
import com.studentjobs.app.feature.subscription.SubscriptionRequestScreen
import com.studentjobs.app.feature.subscription.SubscriptionScreen
import com.studentjobs.app.feature.trust.TrustScreen
import com.studentjobs.app.firebase.firestore.ApplicationService
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.JobService
import com.studentjobs.app.firebase.firestore.NotificationService
import com.studentjobs.app.firebase.firestore.ShiftService
import com.studentjobs.app.firebase.firestore.StudentService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import com.studentjobs.app.feature.notification.NotificationScreen
import com.studentjobs.app.feature.notification.NotificationViewModel
import com.studentjobs.app.feature.notification.NotificationViewModelFactory

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = "jobs",
        modifier = modifier
    ) {

        // ========================================
        // PROFILE
        // ========================================
        composable("profile") {
            ProfileScreen(
                navController = navController,
                onLogout = onLogout
            )
        }

        // ========================================
        // 🔥 NEW: SYSTEM SETTINGS (ĐỒNG BỘ ĐƯỜNG DẪN TRÁNH CRASH)
        // ========================================
        composable("settings_screen") {
            // Anh có thể thay thế Text bằng màn hình Settings thực tế của anh sau này
            Text("Màn hình Cài đặt Hệ thống công dụng chung")
        }

        // Đồng bộ route "settings" từ VerifiedEmployerProfile trỏ thẳng về đây
        composable("settings") {
            Text("Màn hình Cài đặt thông tin riêng cho Nhà tuyển dụng")
        }

        // ========================================
        // SUBSCRIPTION
        // ========================================
        composable("subscription/{role}") { backStackEntry ->
            val role = UserRole.valueOf(
                backStackEntry.arguments?.getString("role") ?: "STUDENT"
            )

            SubscriptionScreen(
                role = role,
                currentPlan = SubscriptionPlan.FREE,
                onUpgradePlusClick = {
                    navController.navigate("subscription_request/${role.name}")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // ========================================
        // SUBSCRIPTION REQUEST
        // ========================================
        composable("subscription_request/{role}") { backStackEntry ->
            val role = UserRole.valueOf(
                backStackEntry.arguments?.getString("role") ?: "STUDENT"
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
        // JOBS & APPLICATIONS
        // ========================================
        composable("jobs") {
            JobEntryScreen(navController)
        }

        composable("my_applications") {
            val viewModel: MyApplicationsViewModel = viewModel()
            MyApplicationsScreen(viewModel = viewModel)
        }

        composable("trust") {
            TrustScreen()
        }

        composable("history") {
            Text("Chưa có lịch sử hoạt động")
        }

        // notification
        composable("notification") {

            val repository =

                NotificationRepository(

                    NotificationService()

                )

            val factory =

                NotificationViewModelFactory(

                    repository

                )

            val viewModel: NotificationViewModel =

                viewModel(

                    factory = factory

                )

            NotificationScreen(

                viewModel = viewModel,

                onBack = {

                    navController.popBackStack()

                }

            )
        }

        // message
        composable("messages") {
            Text("Messages")
        }

        // ========================================
        // STUDENT VERIFICATIONS
        // ========================================
        composable("student_verification") {
            StudentVerificationScreen(navController)
        }

        composable("phone_verification/STUDENT") {
            StudentPhoneVerificationScreen(navController)
        }

        composable("phone_verification/EMPLOYER") {
            EmployerPhoneVerificationScreen(navController)
        }

        composable("email_verification/{role}") { backStackEntry ->
            val role = UserRole.valueOf(
                backStackEntry.arguments?.getString("role") ?: "STUDENT"
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
            ScheduleUploadScreen(navController)
        }

        // ========================================
        // EMPLOYER ACTIONS
        // ========================================
        composable("create_job") {
            val repository = JobRepository(
                jobService = JobService(),
                shiftService = ShiftService(),
                employerService = EmployerService()
            )

            val factory = CreateJobViewModelFactory(repository)
            val viewModel: CreateJobViewModel = viewModel(factory = factory)

            CreateJobScreen(
                employerBusinessName = "",
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSubscription = { navController.navigate("subscription/EMPLOYER") },
                onJobCreated = { navController.popBackStack() }
            )
        }

        composable("employer_job_detail/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            val applicationRepository = ApplicationRepository(ApplicationService())
            val studentRepository = StudentRepository(StudentService())
            val userRepository = UserRepository(UserServiceNew())
            val jobRepository = JobRepository(JobService(), ShiftService(), EmployerService())

            val factory = ApplicantListViewModelFactory(
                applicationRepository,
                studentRepository,
                userRepository,
                jobRepository,
                jobId
            )

            val viewModel: ApplicantListViewModel = viewModel(factory = factory)
            ApplicantListScreen(viewModel = viewModel)
        }

        // ========================================
        // SHARED TOOLS (MAPS & SKILLS)
        // ========================================
        composable("location_picker") {
            LocationPickerScreen(
                onConfirmLocation = { lat, lng ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_lat", lat)
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_lng", lng)
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("manage_skills") {
            ManageSkillsScreen(
                currentCategories = emptyList(),
                currentSkills = emptyList(),
                isPlus = false,
                onSave = { categories, skills ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "selected_categories",
                        categories
                    )
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "selected_skills",
                        skills
                    )
                    navController.popBackStack()
                }
            )
        }

        // ========================================
        // JOB DETAILS
        // ========================================
        composable("job_detail/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            val repository = JobRepository(
                jobService = JobService(),
                shiftService = ShiftService(),
                employerService = EmployerService()
            )
            val applicationRepository = ApplicationRepository(ApplicationService())
            val studentRepository = StudentRepository(StudentService())
            val userRepository = UserRepository(UserServiceNew())
            val notificationRepository =

                NotificationRepository(

                    NotificationService()

                )

            val factory = JobDetailViewModelFactory(repository, jobId)
            val viewModel: JobDetailViewModel = viewModel(factory = factory)

            val applyFactory = ApplyJobViewModelFactory(
                applicationRepository = applicationRepository,

                studentRepository = studentRepository,

                jobRepository = repository,

                userRepository = userRepository,

                notificationRepository = notificationRepository
            )
            val applyViewModel: ApplyJobViewModel = viewModel(factory = applyFactory)

            JobDetailScreen(
                viewModel = viewModel,
                applyViewModel = applyViewModel
            )
        }

        composable("active_job_detail/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            val applicationRepository = ApplicationRepository(ApplicationService())
            val studentRepository = StudentRepository(StudentService())
            val userRepository = UserRepository(UserServiceNew())
            val jobRepository = JobRepository(JobService(), ShiftService(), EmployerService())

            val factory = ActiveJobViewModelFactory(
                applicationRepository,
                studentRepository,
                userRepository,
                jobRepository,
                jobId
            )

            val viewModel: ActiveJobViewModel = viewModel(factory = factory)
            ActiveJobScreen(viewModel = viewModel)
        }
    }
}
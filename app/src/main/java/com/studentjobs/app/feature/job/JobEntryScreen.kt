package com.studentjobs.app.feature.job

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.data.repository.auth.AuthRepository
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.recommendation.RecommendationRepository
import com.studentjobs.app.data.repository.student.StudentRepository
import com.studentjobs.app.feature.job.employer.EmployerJobScreen
import com.studentjobs.app.feature.job.employer.EmployerJobViewModel
import com.studentjobs.app.feature.job.employer.EmployerJobViewModelFactory
import com.studentjobs.app.feature.job.list.JobListScreen
import com.studentjobs.app.feature.job.list.JobListViewModel
import com.studentjobs.app.feature.job.list.JobListViewModelFactory
import com.studentjobs.app.firebase.auth.AuthService
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.JobService
import com.studentjobs.app.firebase.firestore.ShiftService
import com.studentjobs.app.firebase.firestore.StudentService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import com.studentjobs.app.firebase.firestore.VerificationService
import com.studentjobs.app.utils.AppPreferences

@Composable
fun JobEntryScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val role = AppPreferences(context).getUserRole()

    when (role) {
        "STUDENT" -> {
            // 1. Khởi tạo các Service cần thiết
            val jobService = JobService()
            val shiftService = ShiftService()
            val employerService = EmployerService()
            val studentService =
                StudentRepository(StudentService()) // Giả sử StudentService đã tồn tại
            val authService = AuthService()
            val userService = UserServiceNew()
            val verificationService = VerificationService()

            // 2. Khởi tạo các Repository
            val jobRepository = JobRepository(jobService, shiftService, employerService)
            val studentRepository = StudentRepository(StudentService())
            val authRepository = AuthRepository(
                authService,
                userService,
                StudentService(),
                employerService,
                verificationService
            )

            val recommendationRepository =
                RecommendationRepository()

            val factory =
                JobListViewModelFactory(

                    jobRepository =
                        jobRepository,

                    studentRepository =
                        studentRepository,

                    authRepository =
                        authRepository,

                    recommendationRepository =
                        recommendationRepository
                )

            val viewModel: JobListViewModel = viewModel(factory = factory)

            JobListScreen(
                viewModel = viewModel,
                onJobClick = { jobId -> navController.navigate("job_detail/$jobId") },
                onMyJobsClick = { navController.navigate("my_applications") }
            )
        }

        "EMPLOYER" -> {
            val repository = JobRepository(JobService(), ShiftService(), EmployerService())
            val factory = EmployerJobViewModelFactory(repository)
            val viewModel =
                viewModel(factory = factory, modelClass = EmployerJobViewModel::class.java)

            EmployerJobScreen(navController = navController, viewModel = viewModel)
        }
    }
}
package com.studentjobs.app.feature.job

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.feature.job.employer.EmployerJobScreen
import com.studentjobs.app.feature.job.employer.EmployerJobViewModel
import com.studentjobs.app.feature.job.employer.EmployerJobViewModelFactory
import com.studentjobs.app.feature.job.list.JobListScreen
import com.studentjobs.app.feature.job.list.JobListViewModel
import com.studentjobs.app.feature.job.list.JobListViewModelFactory
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.JobService
import com.studentjobs.app.firebase.firestore.ShiftService
import com.studentjobs.app.utils.AppPreferences

@Composable
fun JobEntryScreen(
    navController: NavController
) {

    val role = AppPreferences(
        LocalContext.current
    ).getUserRole()

    when (role) {

        "STUDENT" -> {

            val repository = JobRepository(
                JobService(),
                ShiftService(),
                EmployerService()
            )

            val factory =
                JobListViewModelFactory(
                    repository
                )

            val viewModel:
                    JobListViewModel =
                viewModel(
                    factory = factory
                )

            JobListScreen(

                viewModel = viewModel,

                onJobClick = { jobId ->

                    navController.navigate(
                        "job_detail/$jobId"
                    )
                },

                onMyJobsClick = {

                    navController.navigate(
                        "my_applications"
                    )
                }
            )
        }

        "EMPLOYER" -> {

            val repository = JobRepository(
                JobService(),
                ShiftService(),
                EmployerService()
            )

            val factory =
                EmployerJobViewModelFactory(
                    repository
                )

            val viewModel:
                    EmployerJobViewModel =
                viewModel(
                    factory = factory
                )

            EmployerJobScreen(

                navController =
                    navController,

                viewModel =
                    viewModel
            )
        }
    }
}
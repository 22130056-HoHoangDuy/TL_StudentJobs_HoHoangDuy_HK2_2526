package com.studentjobs.app.feature.job.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.auth.AuthRepository
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.recommendation.RecommendationRepository
import com.studentjobs.app.data.repository.student.StudentRepository

class JobListViewModelFactory(

    private val jobRepository: JobRepository,

    private val studentRepository: StudentRepository,

    private val authRepository: AuthRepository,

    private val recommendationRepository: RecommendationRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                JobListViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")

            return JobListViewModel(

                jobRepository =
                    jobRepository,

                studentRepository =
                    studentRepository,

                authRepository =
                    authRepository,

                recommendationRepository =
                    recommendationRepository

            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
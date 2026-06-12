package com.studentjobs.app.feature.application.employer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.application.ApplicationRepository
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.student.StudentRepository
import com.studentjobs.app.data.repository.user.UserRepository

class ApplicantListViewModelFactory(

    private val applicationRepository:
    ApplicationRepository,

    private val studentRepository:
    StudentRepository,

    private val userRepository:
    UserRepository,

    private val jobRepository:
    JobRepository,

    private val jobId: String

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (

            modelClass.isAssignableFrom(
                ApplicantListViewModel::class.java
            )

        ) {

            return ApplicantListViewModel(

                applicationRepository,

                studentRepository,

                userRepository,

                jobRepository,

                jobId

            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel"
        )
    }
}
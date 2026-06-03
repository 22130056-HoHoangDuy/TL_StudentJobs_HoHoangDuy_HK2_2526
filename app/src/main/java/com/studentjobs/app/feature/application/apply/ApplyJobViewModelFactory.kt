package com.studentjobs.app.feature.application.apply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.application.ApplicationRepository
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.student.StudentRepository
import com.studentjobs.app.data.repository.user.UserRepository

class ApplyJobViewModelFactory(

    private val applicationRepository:
    ApplicationRepository,

    private val studentRepository:
    StudentRepository,

    private val jobRepository:
    JobRepository,

    private val userRepository:
    UserRepository

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(

        modelClass: Class<T>

    ): T {

        return ApplyJobViewModel(

            applicationRepository,

            studentRepository,

            jobRepository,

            userRepository

        ) as T
    }
}
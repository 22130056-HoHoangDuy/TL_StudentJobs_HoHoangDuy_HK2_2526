package com.studentjobs.app.feature.job.employer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.job.JobRepository

class EmployerJobViewModelFactory(

    private val repository: JobRepository

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(

        modelClass: Class<T>

    ): T {

        if (

            modelClass.isAssignableFrom(
                EmployerJobViewModel::class.java
            )

        ) {

            return EmployerJobViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel"
        )
    }
}
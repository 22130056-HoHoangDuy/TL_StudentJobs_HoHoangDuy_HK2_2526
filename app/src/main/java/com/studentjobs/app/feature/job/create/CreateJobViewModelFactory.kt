package com.studentjobs.app.feature.job.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.job.JobRepository

class CreateJobViewModelFactory(

    private val repository: JobRepository

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(

        modelClass: Class<T>

    ): T {

        if (

            modelClass.isAssignableFrom(
                CreateJobViewModel::class.java
            )

        ) {

            return CreateJobViewModel(
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
package com.studentjobs.app.feature.job.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.job.JobRepository

class JobListViewModelFactory(

    private val repository: JobRepository

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
                repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
package com.studentjobs.app.feature.job.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.job.JobRepository

class JobDetailViewModelFactory(

    private val repository: JobRepository,

    private val jobId: String

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return JobDetailViewModel(

            repository,

            jobId

        ) as T
    }
}
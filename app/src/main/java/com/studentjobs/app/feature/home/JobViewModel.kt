package com.studentjobs.app.feature.home

import androidx.lifecycle.ViewModel
import com.studentjobs.app.data.datasource.fakeJobs
import com.studentjobs.app.data.model.job.JobEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JobViewModel : ViewModel() {

    private val _jobs = MutableStateFlow<List<JobEntity>>(emptyList())
    val jobs: StateFlow<List<JobEntity>> = _jobs

    init {
        loadJobs()
    }

    private fun loadJobs() {
        _jobs.value = fakeJobs
    }
}
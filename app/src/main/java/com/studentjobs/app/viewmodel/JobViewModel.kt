package com.studentjobs.app.viewmodel

import androidx.lifecycle.ViewModel
import com.studentjobs.app.data.datasource.fakeJobs
import com.studentjobs.app.data.model.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JobViewModel : ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    init {
        loadJobs()
    }

    private fun loadJobs() {
        _jobs.value = fakeJobs
    }
}
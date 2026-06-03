package com.studentjobs.app.feature.job.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.repository.job.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JobDetailViewModel(

    private val repository: JobRepository,

    private val jobId: String

) : ViewModel() {

    private val _uiState =

        MutableStateFlow(
            JobDetailUiState()
        )

    val uiState: StateFlow<JobDetailUiState> =
        _uiState.asStateFlow()

    init {

        loadJob()
    }

    private fun loadJob() {

        viewModelScope.launch {

            val (job, shifts) =

                repository.getJobWithShifts(
                    jobId
                )

            _uiState.value =

                JobDetailUiState(

                    isLoading = false,

                    job = job,

                    shifts = shifts
                )
        }
    }
}
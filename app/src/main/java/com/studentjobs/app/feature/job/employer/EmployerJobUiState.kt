package com.studentjobs.app.feature.job.employer

import com.studentjobs.app.data.model.job.JobEntity

data class EmployerJobUiState(

    val jobs: List<JobEntity> = emptyList(),

    val activeJobCount: Int = 0,

    val maxJobAllowed: Int = 2,

    val isLoading: Boolean = false,

    val errorMessage: String? = null
)
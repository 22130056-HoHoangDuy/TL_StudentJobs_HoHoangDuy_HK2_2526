package com.studentjobs.app.feature.job.list

import com.studentjobs.app.data.model.job.JobEntity

data class JobListUiState(

    val jobs: List<JobEntity> = emptyList(),

    val isLoading: Boolean = true,

    val searchText: String = ""

)
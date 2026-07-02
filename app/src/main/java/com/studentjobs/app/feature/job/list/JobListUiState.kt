package com.studentjobs.app.feature.job.list

import com.studentjobs.app.data.model.job.JobEntity

data class JobListUiState(

    val jobs: List<JobEntity> = emptyList(),

    val isLoading: Boolean = false,

    val searchText: String = "",

    val isAutoApplyEnabled: Boolean = false,

    val filterDistance: Float = 10.0f,

    val selectedSkills: List<String> = emptyList(),

    val minSalary: Double = 0.0,

    val isViewingSuggested: Boolean = false,

    val studentLatitude: Double? = null,

    val studentLongitude: Double? = null

)
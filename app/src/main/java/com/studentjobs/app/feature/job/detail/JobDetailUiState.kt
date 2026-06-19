package com.studentjobs.app.feature.job.detail

import com.studentjobs.app.data.model.job.JobEntity
import com.studentjobs.app.data.model.job.ShiftEntity

data class JobDetailUiState(

    val isLoading: Boolean = true,

    val job: JobEntity? = null,

    val shifts: List<ShiftEntity> = emptyList()
)
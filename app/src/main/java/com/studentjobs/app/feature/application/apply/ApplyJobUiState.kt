package com.studentjobs.app.feature.application.apply

data class ApplyJobUiState(

    val isLoading: Boolean = false,

    val isSuccess: Boolean = false,

    val hasApplied: Boolean = false,

    val limitType:
    ApplyJobLimitType =
        ApplyJobLimitType.NONE,

    val error: String? = null
)
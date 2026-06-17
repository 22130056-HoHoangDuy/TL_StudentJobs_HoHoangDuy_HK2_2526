package com.studentjobs.app.feature.application.student

import com.studentjobs.app.data.model.application.ApplicationEntity

data class MyApplicationsUiState(

    val pendingApplications:
    List<ApplicationEntity> =
        emptyList(),

    val workingApplications:
    List<ApplicationEntity> =
        emptyList(),

    val isLoading: Boolean =
        false
)
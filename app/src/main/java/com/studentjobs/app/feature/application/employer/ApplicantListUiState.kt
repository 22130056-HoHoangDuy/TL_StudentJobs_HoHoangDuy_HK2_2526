package com.studentjobs.app.feature.application.employer

import com.studentjobs.app.data.model.application.ApplicantItem

data class ApplicantListUiState(

    val isLoading: Boolean = false,

    val applicants:
    List<ApplicantItem> = emptyList(),

    val errorMessage: String? = null
)
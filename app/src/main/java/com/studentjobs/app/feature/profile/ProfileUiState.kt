package com.studentjobs.app.feature.profile

import com.studentjobs.app.data.model.UserRole

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val trustScore: Int = 0,
    val isLoading: Boolean = true,
    val role : UserRole = UserRole.STUDENT
)
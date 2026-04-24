package com.studentjobs.app.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.STUDENT,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val isStudentVerified: Boolean = false,
    val isBusinessVerified: Boolean = false
)
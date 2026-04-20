package com.studentjobs.app.data.model

data class User(
    val uid: String,
    val email: String,
    val role: UserRole = UserRole.STUDENT
)
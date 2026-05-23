package com.studentjobs.app.data.model.student

data class StudentProfile(

    // uid
    val uid: String = "",

    // basic information
    val fullName: String = "",
    val avatarUrl: String? = null,
    val gender: String? = null,
    val bio: String? = null,

    // academic information
    val schoolName: String? = null,
    val major: String? = null,

    val studentId: String? = null,

    val dateOfBirth: String? = null,

    // student email
    val studentEmail: String? = null,

    // skill
    val skills: List<String> = emptyList(),

    // student location
    val studentLatitude: Double? = null,
    val studentLongitude: Double? = null,
    val studentLocationUrl: String? = null,

    // job reference
    val preferredJobCategories: List<String> = emptyList(),
    val preferredSalaryMin: Double? = null,

    // system log
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
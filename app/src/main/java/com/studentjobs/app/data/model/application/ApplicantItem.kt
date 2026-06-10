package com.studentjobs.app.data.model.application

import com.studentjobs.app.data.model.student.StudentProfile
import com.studentjobs.app.data.model.user.UserCore

data class ApplicantItem(

    val application:
    ApplicationEntity,

    val studentProfile:
    StudentProfile? = null,

    val userCore:
    UserCore? = null
)
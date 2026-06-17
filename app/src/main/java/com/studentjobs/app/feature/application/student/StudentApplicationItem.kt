package com.studentjobs.app.feature.application.student

import com.studentjobs.app.data.model.application.ApplicationEntity

data class StudentApplicationItem(

    val application: ApplicationEntity,

    val employerPhone: String? = null
)
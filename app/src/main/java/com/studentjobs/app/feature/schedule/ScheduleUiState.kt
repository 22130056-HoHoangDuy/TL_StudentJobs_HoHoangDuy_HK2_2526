package com.studentjobs.app.feature.schedule

import android.net.Uri
import com.studentjobs.app.data.model.student.StudentSchedule

data class ScheduleUiState(

    val isLoading: Boolean = false,

    val currentSchedule:
    StudentSchedule? = null,

    val selectedImageUri: Uri? = null,

    val extractedText: String = "",

    val uploadSuccess: Boolean = false,

    val errorMessage: String? = null
)
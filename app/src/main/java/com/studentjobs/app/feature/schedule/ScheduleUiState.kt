package com.studentjobs.app.feature.schedule

import android.net.Uri
import com.studentjobs.app.data.model.student.StudentSchedule

data class ScheduleUiState(

    // LOADING
    val isLoading: Boolean = false,

    // IMAGE
    val selectedImageUri: Uri? = null,

    val uploadedImageUrl: String? = null,

    // OCR
    val isProcessingOcr: Boolean = false,

    // SCHEDULE
    val schedule: StudentSchedule? = null,

    // UI
    val successMessage: String? = null,

    val errorMessage: String? = null
)
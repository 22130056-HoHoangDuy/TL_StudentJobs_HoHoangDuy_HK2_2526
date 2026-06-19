package com.studentjobs.app.data.model.student

import java.util.Date

data class StudentSchedule(

    val uid: String = "",

    val busySlots:
    List<BusyTimeSlot> = emptyList(),

    val daysWithSchedule:
    List<Int> = emptyList(),

    val timetableImageUrl: String? = null,

    val ocrProcessed: Boolean = false,

    val ocrConfidence: Double = 0.0,

    val source: String = "OCR",

    val createdAt: Date? = null,

    val updatedAt: Date? = null
)

data class BusyTimeSlot(

    val subjectName: String = "",

    val dayOfWeek: Int = 1,

    val startMinute: Int = 0,

    val endMinute: Int = 0
)
package com.studentjobs.app.data.model.student

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

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)

data class BusyTimeSlot(

    val subjectName: String = "",

    val dayOfWeek: Int = 1,

    val startMinute: Int = 0,

    val endMinute: Int = 0
)
package com.studentjobs.app.data.model.student

data class StudentSchedule(

    // ========================================
    // OWNER
    // ========================================

    val uid: String = "",

    // ========================================
    // SCHEDULE DATA
    // ========================================

    val schedules: List<ScheduleItem> =
        emptyList(),

    // ========================================
    // OCR
    // ========================================

    val timetableImageUrl: String? = null,

    val ocrProcessed: Boolean = false,

    // OCR | MANUAL
    val source: String = "OCR",

    // ========================================
    // SEMESTER
    // ========================================

    val semester: String = "",

    // ========================================
    // SYSTEM
    // ========================================

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)

data class ScheduleItem(

    // ========================================
    // SUBJECT
    // ========================================

    val subjectName: String = "",

    // ========================================
    // DAY OF WEEK
    // ========================================

    // 1 = Monday
    // 2 = Tuesday
    // ...
    // 7 = Sunday

    val dayOfWeek: Int = 1,

    // ========================================
    // TIME
    // ========================================

    // minutes from 00:00
    // 9:30 = 570
    // 12:15 = 735

    val startMinute: Int = 0,

    val endMinute: Int = 0
)
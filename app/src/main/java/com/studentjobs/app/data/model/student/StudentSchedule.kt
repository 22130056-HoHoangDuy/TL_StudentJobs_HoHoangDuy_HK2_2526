package com.studentjobs.app.data.model.student

data class StudentSchedule(
// ===== OWNER =====
    val uid: String = "",
// ===== SCHEDULES =====
    val schedules: List<ScheduleItem> = emptyList(),
// ===== OCR =====
    val timetableImageUrl: String? = null,
    val ocrProcessed: Boolean = false,
// ===== SYSTEM =====
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

data class ScheduleItem(
    val subjectName: String = "",
    val dayOfWeek: String = "",
    val startTime: String = "",
    val endTime: String = ""
)

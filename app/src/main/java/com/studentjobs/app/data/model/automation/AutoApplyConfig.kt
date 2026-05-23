package com.studentjobs.app.data.model.automation

data class AutoApplyConfig(

    // ===== OWNER =====
    val studentUid: String = "",

    // ===== STATUS =====
    val enabled: Boolean = false,

    // ===== FILTER =====
    val preferredCategories: List<String> =
        emptyList(),

    val preferredLocations: List<String> =
        emptyList(),

    val minimumSalary: Double? = null,

    // ===== MATCHING =====
    val requiredSkillMatching: Boolean = true,

    val trustFilteringEnabled: Boolean = true,

    val scheduleConflictCheckEnabled: Boolean = true,

    // ===== LIMIT =====
    val maxPendingApplications: Int = 5,

    // ===== SYSTEM =====
    val updatedAt: Long = 0L
)
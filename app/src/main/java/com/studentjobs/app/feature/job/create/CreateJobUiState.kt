package com.studentjobs.app.feature.job.create

import com.studentjobs.app.data.model.job.ShiftEntity

data class CreateJobUiState(

    // ========================================
    // BASIC
    // ========================================
    val title: String = "",

    val description: String = "",

    // ========================================
    // SALARY
    // ========================================

    val salaryMin: String = "",

    val salaryMax: String = "",

    // ========================================
    // RECRUITMENT
    // ========================================

    val requiredApplicants: String = "1",

    // ========================================
    // SKILLS
    // ========================================

    val availableSkills:
    List<String> = emptyList(),

    val selectedSkills:
    List<String> = emptyList(),

    // ========================================
    // SHIFT
    // ========================================

    val shifts:
    List<ShiftEntity> = emptyList(),

    // ========================================
    // AUTO RECRUITMENT
    // ========================================

    val autoRecruitmentEnabled:
    Boolean = false,

    val isPlusEmployer:
    Boolean = false,

    // ========================================
    // SYSTEM
    // ========================================

    val isLoading: Boolean = false,

    val success: Boolean = false,

    val errorMessage: String? = null,

    val draftJobId: String = "",

    // ========================================
// SHIFT INPUT
// ========================================

    val selectedDay: Int = 1,

    val startMinute: String = "",

    val endMinute: String = "",

    val slots: String = "1",
)
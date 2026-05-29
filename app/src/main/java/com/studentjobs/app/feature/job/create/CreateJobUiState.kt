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

    // ========================================
    // SYSTEM
    // ========================================

    val isLoading: Boolean = false,

    val success: Boolean = false,

    val errorMessage: String? = null,

    val draftJobId: String = "",

    val startTime: String = "",
    val endTime: String = ""
)
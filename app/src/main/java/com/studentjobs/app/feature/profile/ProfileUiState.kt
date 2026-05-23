package com.studentjobs.app.feature.profile

import com.studentjobs.app.data.model.employer.EmployerProfile
import com.studentjobs.app.data.model.employer.EmployerVerification
import com.studentjobs.app.data.model.student.StudentProfile
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.data.model.user.UserRole

data class ProfileUiState(

    // ===== SYSTEM =====
    val isLoading: Boolean = true,

    // ===== USER =====
    val userCore: UserCore? = null,

    // ===== ROLE =====
    val role: UserRole = UserRole.STUDENT,

    // ===== STUDENT =====
    val studentProfile: StudentProfile? = null,

    val studentVerification: StudentVerification? = null,

    // ===== EMPLOYER =====
    val employerProfile: EmployerProfile? = null,

    val employerVerification: EmployerVerification? = null
)
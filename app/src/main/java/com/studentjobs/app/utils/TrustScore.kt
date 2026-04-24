package com.studentjobs.app.utils

import com.studentjobs.app.data.model.User
import com.studentjobs.app.data.model.UserRole

fun calculateTrustScore(user: User): Int {

    var score = 0

    if (user.isEmailVerified) score += 30
    if (user.isPhoneVerified) score += 30

    when (user.role) {
        UserRole.STUDENT -> {
            if (user.isStudentVerified) score += 40
        }
        UserRole.EMPLOYER -> {
            if (user.isBusinessVerified) score += 40
        }
    }

    return score
}
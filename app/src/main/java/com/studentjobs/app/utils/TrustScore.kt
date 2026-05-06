package com.studentjobs.app.utils

import com.studentjobs.app.data.model.User
import com.studentjobs.app.data.model.UserRole

fun calculateTrustScore(user: User): Int {

    var score = 0

    if (user.isEmailVerified) score += 20
    if (user.isPhoneVerified) score += 10

    when (user.role) {
        UserRole.STUDENT -> {
            if (user.isStudentVerified) score += 20
        }
        UserRole.EMPLOYER -> {
            if (user.isBusinessVerified) score += 20
        }
    }

    return score
}
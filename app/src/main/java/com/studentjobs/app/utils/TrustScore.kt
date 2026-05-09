package com.studentjobs.app.utils

import com.studentjobs.app.data.model.User
import com.studentjobs.app.data.model.UserRole

fun calculateTrustScore(user: User): Int {

    var score = 0

    // ===== LOGIN EMAIL =====
    if (user.isEmailVerified) {
        score += 20
    }

    // ===== PHONE =====
    if (user.isPhoneVerified) {
        score += 10
    }

    when (user.role) {

        // ===== STUDENT =====
        UserRole.STUDENT -> {

            // OCR student card
            if (user.isStudentVerified) {
                score += 20
            }

            // Student domain email
            if (user.isStudentEmailVerified) {
                score += 20
            }
        }

        // ===== EMPLOYER =====
        UserRole.EMPLOYER -> {

            if (user.isBusinessVerified) {
                score += 20
            }
        }
    }

    return score
}
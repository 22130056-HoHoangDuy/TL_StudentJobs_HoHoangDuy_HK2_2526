package com.studentjobs.app.utils

import com.studentjobs.app.data.model.employer.EmployerVerification
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.data.model.user.UserRole

fun calculateTrustScore(
    user: UserCore,
    studentVerification: StudentVerification? = null,
    employerVerification: EmployerVerification? = null
): Int {
    var trustScore = 0
    // calculate trust score by role
    when (user.role) {
        // by role = STUDENT
        UserRole.STUDENT -> {
            // done verified school email
            if (
                studentVerification
                    ?.studentEmailVerified == VerificationStatus.VERIFIED
            ) {
                trustScore += 15
            }

            // done verified student card
            if (
                studentVerification
                    ?.studentCardVerified == VerificationStatus.VERIFIED
            ) {
                trustScore += 20
            }

            // done verified student phone
            if (studentVerification
                    ?.studentPhoneVerified == VerificationStatus.VERIFIED
            ) {
                trustScore += 15
            }
        }

        // by role = EMPLOYER
        UserRole.EMPLOYER -> {
            // done verified license
            if (
                employerVerification
                    ?.businessLicenseVerified == VerificationStatus.VERIFIED
            ) {

                trustScore += 20
            }

            // done verified phone
            if (
                employerVerification
                    ?.businessPhoneVerified == VerificationStatus.VERIFIED
            ) {

                trustScore += 15
            }

            // done verified email
            if (
                employerVerification
                    ?.businessEmailVerified == VerificationStatus.VERIFIED
            ) {

                trustScore += 15
            }
        }
    }
    return trustScore
}
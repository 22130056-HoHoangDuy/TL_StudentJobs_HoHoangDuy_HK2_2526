package com.studentjobs.app.data.manager.verification

import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.data.repository.profile.ProfileRepository

class VerificationManager(

    private val repository: ProfileRepository

) {

    // ========================================
    // CHECK STUDENT VERIFIED
    // ========================================

    fun canAutoVerifyStudent(

        verification: StudentVerification?

    ): Boolean {

        if (verification == null) return false

        return (

                verification.studentEmailVerified == VerificationStatus.VERIFIED

                        &&

                        verification.studentPhoneVerified == VerificationStatus.VERIFIED

                        &&

                        verification.studentCardVerified == VerificationStatus.VERIFIED)
    }

    // ========================================
    // AUTO VERIFY STUDENT
    // ========================================

    suspend fun autoVerifyStudent(
        uid: String
    ): Result<Unit> {

        return try {

            val verification = repository.getStudentVerification(uid)

            val canVerify =

                canAutoVerifyStudent(
                    verification
                )

            if (!canVerify) {

                return Result.failure(

                    Exception(
                        "Student verification conditions not completed"
                    )
                )
            }

            // ========================================
            // UPDATE USER VERIFIED
            // ========================================

            repository.updateStudentVerifiedStatus(

                uid = uid,

                verified = true
            )

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
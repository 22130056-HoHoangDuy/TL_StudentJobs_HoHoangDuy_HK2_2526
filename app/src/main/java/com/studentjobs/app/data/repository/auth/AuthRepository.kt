package com.studentjobs.app.data.repository.auth

import com.studentjobs.app.data.model.employer.EmployerProfile
import com.studentjobs.app.data.model.employer.EmployerVerification
import com.studentjobs.app.data.model.student.StudentProfile
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.model.user.UserStatus
import com.studentjobs.app.firebase.auth.AuthService
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.StudentService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import com.studentjobs.app.firebase.firestore.VerificationService

class AuthRepository(

    private val authService: AuthService,

    private val userService: UserServiceNew,

    private val studentService: StudentService,

    private val employerService: EmployerService,

    private val verificationService: VerificationService

) {

    // ========================================
    // REGISTER
    // ========================================
    suspend fun register(

        email: String,

        password: String,

        role: UserRole

    ): Result<UserCore> {

        return try {

            // =========================
            // FIREBASE AUTH
            // =========================

            val result = authService
                .register(email, password)

            val uid = result.user?.uid
                ?: return Result.failure(
                    Exception("User ID is null")
                )

            // =========================
            // CREATE USER CORE
            // =========================

            val userCore = UserCore(

                uid = uid,

                role = role,

                email = email,

                isEmailVerified = false,

                isPhoneVerified = false,

                trustScore = 0,

                subscriptionPlan =
                    SubscriptionPlan.FREE,

                status =
                    UserStatus.ACTIVE,

                createdAt =
                    System.currentTimeMillis(),

                updatedAt =
                    System.currentTimeMillis()
            )

            val userResult =
                userService.createUserCore(userCore)

            if (userResult.isFailure) {

                return Result.failure(
                    userResult.exceptionOrNull()
                        ?: Exception(
                            "Failed to create user"
                        )
                )
            }

            // =========================
            // CREATE STUDENT DOMAIN
            // =========================

            if (role == UserRole.STUDENT) {

                val studentProfile =
                    StudentProfile(

                        uid = uid,

                        studentEmail = email,

                        createdAt =
                            System.currentTimeMillis(),

                        updatedAt =
                            System.currentTimeMillis()
                    )

                val studentVerification =
                    StudentVerification(

                        uid = uid,

                        studentEmail = email,

                        submittedAt =
                            System.currentTimeMillis()
                    )

                studentService
                    .createStudentProfile(
                        studentProfile
                    )

                verificationService
                    .createStudentVerification(
                        studentVerification
                    )
            }

            // =========================
            // CREATE EMPLOYER DOMAIN
            // =========================

            if (role == UserRole.EMPLOYER) {

                val employerProfile =
                    EmployerProfile(

                        uid = uid,

                        createdAt =
                            System.currentTimeMillis(),

                        updatedAt =
                            System.currentTimeMillis()
                    )

                val employerVerification =
                    EmployerVerification(

                        uid = uid,

                        submittedAt =
                            System.currentTimeMillis()
                    )

                employerService
                    .createEmployerProfile(
                        employerProfile
                    )

                verificationService
                    .createEmployerVerification(
                        employerVerification
                    )
            }

            Result.success(userCore)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // LOGIN
    // ========================================
    suspend fun login(

        email: String,

        password: String

    ): Result<UserCore> {

        return try {

            val result = authService
                .login(email, password)

            val uid = result.user?.uid
                ?: return Result.failure(
                    Exception("User ID is null")
                )

            val userCore =
                userService.getUserCore(uid)

                    ?: return Result.failure(
                        Exception(
                            "User not found"
                        )
                    )

            Result.success(userCore)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }
}
package com.studentjobs.app.data.repository.auth

import com.google.firebase.auth.FirebaseUser
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
import java.util.Date

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

            val result =
                authService.register(
                    email,
                    password
                )

            val uid = result.user?.uid
                ?: return Result.failure(
                    Exception("User ID is null")
                )

            val userCore = UserCore(

                uid = uid,

                role = role,

                email = email,

                phoneNumber = null,

                userVerified = false,

                trustScore = 0,

                subscriptionPlan = SubscriptionPlan.FREE,

                status = UserStatus.ACTIVE,

                createdAt = Date(),

                updatedAt = Date()
            )

            val userResult =
                userService.createUserCore(userCore)

            if (userResult.isFailure) {

                return Result.failure(

                    userResult.exceptionOrNull()
                        ?: Exception("Failed to create user")
                )
            }

            if (role == UserRole.STUDENT) {

                studentService.createStudentProfile(

                    StudentProfile(

                        uid = uid,

                        studentEmail = email,

                        createdAt = Date(),

                        updatedAt = Date()
                    )
                )

                verificationService.createStudentVerification(

                    StudentVerification(

                        uid = uid,

                        createdAt = Date(),

                        updatedAt = Date()
                    )
                )
            }

            if (role == UserRole.EMPLOYER) {

                employerService.createEmployerProfile(

                    EmployerProfile(

                        uid = uid,

                        createdAt = Date(),

                        updatedAt = Date()
                    )
                )

                verificationService.createEmployerVerification(

                    EmployerVerification(

                        uid = uid,

                        submittedAt = Date(),

                        reviewedAt = Date()
                    )
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

            val result =
                authService.login(
                    email,
                    password
                )

            val uid =
                result.user?.uid
                    ?: return Result.failure(
                        Exception("User ID is null")
                    )

            val userCore =
                userService.getUserCore(uid)
                    ?: return Result.failure(
                        Exception("User not found")
                    )

            Result.success(userCore)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // SESSION
    // ========================================

    fun getCurrentUser(): FirebaseUser? {

        return authService.getCurrentUser()
    }

    fun getCurrentUserUid(): String? {

        return authService
            .getCurrentUser()
            ?.uid
    }

    fun isLoggedIn(): Boolean {

        return authService.getCurrentUser() != null
    }

    // ========================================
    // LOGOUT
    // ========================================

    fun logout() {

        authService.logout()
    }

    // ========================================
    // FORGOT PASSWORD
    // ========================================

    suspend fun forgotPassword(
        email: String
    ): Result<Unit> {

        return try {

            val user =
                userService.getUserByEmail(email)

            if (user == null) {

                return Result.failure(
                    Exception("Email chưa được đăng ký")
                )
            }

            authService.sendPasswordResetEmail(email)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
package com.studentjobs.app.data.repository

import com.studentjobs.app.firebase.auth.AuthService
import com.studentjobs.app.firebase.firestore.UserService

class AuthRepository(
    private val authService: AuthService,
    private val userService: UserService
) {

    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            val result = authService.register(email, password)

            val uid = result.user?.uid
                ?: return Result.failure(Exception("UID null"))

            val userData = mapOf(
                "uid" to uid,
                "email" to email,
                "role" to "student"
            )

            val firestoreResult = userService.createUser(uid, userData)

            if (firestoreResult.isFailure) {
                return Result.failure(firestoreResult.exceptionOrNull()!!)
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            authService.login(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

}
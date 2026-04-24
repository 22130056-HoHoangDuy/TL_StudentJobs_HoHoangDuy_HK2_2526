package com.studentjobs.app.data.repository

import com.studentjobs.app.data.model.User
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.firebase.auth.AuthService
import com.studentjobs.app.firebase.firestore.UserService

class AuthRepository(
    private val authService: AuthService,
    private val userService: UserService
) {

    suspend fun register(
        email: String,
        password: String,
        role: UserRole
    ): Result<User> {
        return try {
            val result = authService.register(email, password)

            val uid = result.user?.uid
                ?: return Result.failure(Exception("User ID is null"))

            // ✅ dùng role từ UI (quan trọng)
            val user = User(
                uid = uid,
                email = email,
                role = role
            )

            // ✅ save Firestore
            val firestoreResult = userService.createUser(user)

            if (firestoreResult.isFailure) {
                return Result.failure(
                    firestoreResult.exceptionOrNull()
                        ?: Exception("Failed to save user")
                )
            }

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = authService.login(email, password)

            val uid = result.user?.uid
                ?: return Result.failure(Exception("User ID is null"))

            // ✅ LẤY USER TỪ FIRESTORE (QUAN TRỌNG)
            val user = userService.getUser(uid)
                ?: return Result.failure(Exception("User not found in Firestore"))

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
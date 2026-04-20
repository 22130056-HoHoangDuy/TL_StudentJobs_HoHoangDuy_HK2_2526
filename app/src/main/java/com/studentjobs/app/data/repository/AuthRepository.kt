package com.studentjobs.app.data.repository

import com.studentjobs.app.data.model.User
import com.studentjobs.app.data.model.UserRole
import com.studentjobs.app.firebase.auth.AuthService
import com.studentjobs.app.firebase.firestore.UserService

class AuthRepository(
    private val authService: AuthService,
    private val userService: UserService
) {

    suspend fun register(email: String, password: String): Result<User> {
        return try {
            val result = authService.register(email, password)

            val uid = result.user?.uid
                ?: return Result.failure(Exception("User ID is null"))

            // 👉 Tạo object User thay vì map
            val user = User(
                uid = uid,
                email = email,
                role = UserRole.STUDENT
            )

            // 👉 Gửi User xuống Firestore
            val firestoreResult = userService.createUser(user)

            if (firestoreResult.isFailure) {
                return Result.failure(
                    firestoreResult.exceptionOrNull() ?: Exception("Failed to save user")
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

            // 👉 Tạm thời tạo user cơ bản (sau này có thể fetch từ Firestore)
            val user = User(
                uid = uid,
                email = email
            )

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
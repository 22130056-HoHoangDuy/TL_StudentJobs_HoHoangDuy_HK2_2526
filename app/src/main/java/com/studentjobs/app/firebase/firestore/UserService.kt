package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.User
import com.studentjobs.app.data.model.UserRole
import kotlinx.coroutines.tasks.await

class UserService {

    private val db = FirebaseFirestore.getInstance()

    // =========================
    // CREATE USER
    // =========================
    suspend fun createUser(user: User): Result<Unit> {
        return try {

            val userMap = mapOf(
                "uid" to user.uid,
                "name" to user.name,
                "email" to user.email,
                "role" to user.role.name, // 🔥 enum → string
                "isEmailVerified" to user.isEmailVerified,
                "isPhoneVerified" to user.isPhoneVerified,
                "isStudentVerified" to user.isStudentVerified,
                "isBusinessVerified" to user.isBusinessVerified
            )

            db.collection("users")
                .document(user.uid)
                .set(userMap)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // =========================
    // GET USER
    // =========================
    suspend fun getUser(uid: String): User? {
        return try {

            val doc = db.collection("users")
                .document(uid)
                .get()
                .await()

            if (!doc.exists()) return null

            val roleString = doc.getString("role") ?: "STUDENT"

            doc.toObject(User::class.java)?.copy(
                role = UserRole.valueOf(roleString)
            )

        } catch (e: Exception) {
            null
        }
    }
}
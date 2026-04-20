package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.User
import kotlinx.coroutines.tasks.await

class UserService {

    private val db = FirebaseFirestore.getInstance()

    suspend fun createUser(user: User): Result<Unit> {
        return try {
            val userMap = mapOf(
                "uid" to user.uid,
                "email" to user.email,
                "role" to user.role.name
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
}
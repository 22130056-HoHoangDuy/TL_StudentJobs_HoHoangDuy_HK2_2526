package com.studentjobs.app.firebase.firestore

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.studentjobs.app.data.model.User
import com.studentjobs.app.data.model.UserRole
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserService {

    private val db = FirebaseFirestore.getInstance()

    // =========================
    // CREATE USER
    // =========================
    suspend fun createUser(user: User): Result<Unit> {
        return try {

            val userMap = mutableMapOf<String, Any>(
                "uid" to user.uid,
                "name" to user.name,
                "email" to user.email,
                "role" to user.role.name,
                "isEmailVerified" to user.isEmailVerified,
                "isPhoneVerified" to user.isPhoneVerified,
                "isStudentVerified" to user.isStudentVerified,
                "isBusinessVerified" to user.isBusinessVerified
            )

            // 👉 chỉ add nếu có dữ liệu
            user.school?.let { userMap["school"] = it }
            user.dateOfBirth?.let { userMap["dateOfBirth"] = it }
            user.avatarUrl?.let { userMap["avatarUrl"] = it }

            db.collection("users")
                .document(user.uid)
                .set(userMap) // hoặc .set(userMap, SetOptions.merge())
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

            val role = try {
                UserRole.valueOf(
                    doc.getString("role")?.uppercase() ?: "STUDENT"
                )
            } catch (e: Exception) {
                UserRole.STUDENT
            }

            User(
                uid = doc.getString("uid") ?: "",
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?: "",
                role = role,
                isEmailVerified = doc.getBoolean("isEmailVerified") ?: false,
                isPhoneVerified = doc.getBoolean("isPhoneVerified") ?: false,
                isStudentVerified = doc.getBoolean("isStudentVerified") ?: false,
                isBusinessVerified = doc.getBoolean("isBusinessVerified") ?: false,
                extractedName = doc.getString("extractedName"),
                studentId = doc.getString("studentId"),
                school = doc.getString("school"),
                dateOfBirth = doc.getString("dateOfBirth"),
                avatarUrl = doc.getString("avatarUrl")
            )

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // =========================
    // UPDATE STUDENT CARD
    // =========================
    suspend fun updateStudentCard(
        uid: String,
        frontUrl: String,
        backUrl: String
    ): Result<Unit> {
        return try {

            db.collection("users")
                .document(uid)
                .update(
                    mapOf(
                        "studentCardFront" to frontUrl,
                        "studentCardBack" to backUrl
                    )
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // =========================
    // REALTIME LISTENER
    // =========================
    fun listenUser(
        uid: String,
        onChange: (User) -> Unit
    ) {
        db.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e("FIRESTORE", "Listen failed", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {

                    val role = try {
                        UserRole.valueOf(
                            snapshot.getString("role")?.uppercase() ?: "STUDENT"
                        )
                    } catch (e: Exception) {
                        UserRole.STUDENT
                    }

                    val user = User(
                        uid = snapshot.getString("uid") ?: "",
                        name = snapshot.getString("name") ?: "",
                        email = snapshot.getString("email") ?: "",
                        role = role,
                        isEmailVerified = snapshot.getBoolean("isEmailVerified") ?: false,
                        isPhoneVerified = snapshot.getBoolean("isPhoneVerified") ?: false,
                        isStudentVerified = snapshot.getBoolean("isStudentVerified") ?: false,
                        isBusinessVerified = snapshot.getBoolean("isBusinessVerified") ?: false,

                        extractedName = snapshot.getString("extractedName"),
                        studentId = snapshot.getString("studentId"),

                        school = snapshot.getString("school"),
                        dateOfBirth = snapshot.getString("dateOfBirth"),
                        avatarUrl = snapshot.getString("avatarUrl")
                    )

                    Log.d("LISTENER", "User updated: $user")

                    onChange(user)
                }
            }
    }

    suspend fun uploadStudentCard(
        uid: String,
        frontUri: Uri,
        backUri: Uri
    ): Result<Unit> {

        return try {

            val storage = FirebaseStorage.getInstance().reference

            // 🔥 path giống Cloud Function đang dùng
            val frontRef = storage.child("student_cards/$uid/front_${UUID.randomUUID()}.jpg")
            val backRef = storage.child("student_cards/$uid/back_${UUID.randomUUID()}.jpg")

            // upload
            frontRef.putFile(frontUri).await()
            backRef.putFile(backUri).await()

            // lấy URL
            val frontUrl = frontRef.downloadUrl.await().toString()
            val backUrl = backRef.downloadUrl.await().toString()

            // update Firestore
            updateStudentCard(uid, frontUrl, backUrl)

            Result.success(Unit)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserService {

    private val db = FirebaseFirestore.getInstance()

//    suspend fun createUser(uid: String, data: Map<String, Any>): Result<Unit> {
//        return try {
//            db.collection("users")
//                .document(uid)
//                .set(data)
//                .await()
//
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
suspend fun createUser(uid: String, data: Map<String, Any>): Result<Unit> {
    return try {
        db.collection("users")
            .document(uid)
            .set(data)
            .await()

        Result.success(Unit)
    } catch (e: Exception) {
        e.printStackTrace() // 👈 thêm dòng này
        Result.failure(e)
    }
}
}
package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.trust.TrustLog
import kotlinx.coroutines.tasks.await

class TrustService {

    private val db =
        FirebaseFirestore.getInstance()

    suspend fun addTrustLog(
        log: TrustLog
    ): Result<Unit> {

        return try {

            db.collection("trust_logs")
                .document(log.trustLogId)
                .set(log)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    suspend fun getTrustLogs(
        uid: String
    ): List<TrustLog> {

        return try {

            db.collection("trust_logs")

                .whereEqualTo(
                    "userUid",
                    uid
                )

                .get()
                .await()

                .toObjects(
                    TrustLog::class.java
                )

                .sortedByDescending {
                    it.createdAt
                }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }
}
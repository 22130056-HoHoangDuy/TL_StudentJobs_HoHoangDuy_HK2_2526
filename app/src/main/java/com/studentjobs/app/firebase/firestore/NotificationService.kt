package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.notification.NotificationEntity
import kotlinx.coroutines.tasks.await

class NotificationService {

    private val db = FirebaseFirestore.getInstance()

    companion object {

        private const val COLLECTION = "notifications"
    }

    suspend fun createNotification(
        notification: NotificationEntity
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)
                .document(notification.notificationId)
                .set(notification)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    suspend fun getNotifications(
        receiverUid: String
    ): List<NotificationEntity> {

        return try {

            db.collection(COLLECTION)

                .whereEqualTo(
                    "receiverUid",
                    receiverUid
                )

                .get()
                .await()

                .toObjects(NotificationEntity::class.java)

                .sortedByDescending {
                    it.createdAt
                }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun markAsRead(
        notificationId: String
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)

                .document(notificationId)

                .update(
                    "isRead",
                    true
                )

                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }
}
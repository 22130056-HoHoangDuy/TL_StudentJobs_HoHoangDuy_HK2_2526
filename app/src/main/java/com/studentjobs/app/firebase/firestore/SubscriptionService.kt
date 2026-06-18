package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.subscription.SubscriptionRequest
import com.studentjobs.app.data.model.subscription.SubscriptionRequestStatus
import com.studentjobs.app.data.model.user.SubscriptionPlan
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class SubscriptionService(

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

) {

    companion object {

        private const val USERS = "users"

        private const val SUBSCRIPTION_REQUESTS =
            "subscription_requests"
    }

    /**
     * Create new PLUS request
     */
    suspend fun createSubscriptionRequest(

        request: SubscriptionRequest

    ): Result<Unit> {

        return try {

            val requestId = UUID.randomUUID().toString()

            val finalRequest = request.copy(

                requestId = requestId,

                requestedAt = Date(),

                status = SubscriptionRequestStatus.PENDING
            )

            firestore
                .collection(SUBSCRIPTION_REQUESTS)
                .document(requestId)
                .set(finalRequest)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    /**
     * Get pending requests for admin
     */
    suspend fun getPendingRequests():

            Result<List<SubscriptionRequest>> {

        return try {

            val snapshot = firestore
                .collection(SUBSCRIPTION_REQUESTS)
                .whereEqualTo(
                    "status",
                    SubscriptionRequestStatus.PENDING.name
                )
                .get()
                .await()

            val requests = snapshot.documents.mapNotNull {

                it.toObject(SubscriptionRequest::class.java)
            }

            Result.success(requests)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    /**
     * Approve subscription request
     */
    suspend fun approveRequest(

        request: SubscriptionRequest,

        adminUid: String

    ): Result<Unit> {

        return try {

            val now = Date()

            val durationMillis =
                request.durationDays *
                        24L *
                        60L *
                        60L *
                        1000L

            val expiredAt =
                Date(now.time + durationMillis)

            firestore.runBatch { batch ->

                /**
                 * Update request status
                 */
                val requestRef = firestore
                    .collection(SUBSCRIPTION_REQUESTS)
                    .document(request.requestId)

                batch.update(
                    requestRef,
                    mapOf(
                        "status" to
                                SubscriptionRequestStatus.APPROVED.name,

                        "reviewedBy" to adminUid,

                        "reviewedAt" to now
                    )
                )

                /**
                 * Update user subscription
                 */
                val userRef = firestore
                    .collection(USERS)
                    .document(request.userUid)

                batch.update(
                    userRef,
                    mapOf(
                        "subscriptionPlan" to
                                SubscriptionPlan.PLUS.name,

                        "subscriptionExpiredAt" to expiredAt
                    )
                )

            }.await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    /**
     * Reject request
     */
    suspend fun rejectRequest(

        requestId: String,

        adminUid: String,

        reason: String

    ): Result<Unit> {

        return try {

            firestore
                .collection(SUBSCRIPTION_REQUESTS)
                .document(requestId)
                .update(
                    mapOf(

                        "status" to
                                SubscriptionRequestStatus.REJECTED.name,

                        "reviewedBy" to adminUid,

                        "reviewNote" to reason,

                        "reviewedAt" to Date()
                    )
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
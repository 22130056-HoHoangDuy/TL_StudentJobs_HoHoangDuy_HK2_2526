package com.studentjobs.app.data.repository.subscription

import com.studentjobs.app.data.model.subscription.SubscriptionRequest
import com.studentjobs.app.firebase.firestore.SubscriptionService

class SubscriptionRepository(

    private val subscriptionService: SubscriptionService =
        SubscriptionService()

) {

    /**
     * Create new subscription request
     */
    suspend fun createSubscriptionRequest(

        request: SubscriptionRequest

    ): Result<Unit> {

        return subscriptionService
            .createSubscriptionRequest(request)
    }

    /**
     * Get pending requests
     */
    suspend fun getPendingRequests():

            Result<List<SubscriptionRequest>> {

        return subscriptionService
            .getPendingRequests()
    }

    /**
     * Approve request
     */
    suspend fun approveRequest(

        request: SubscriptionRequest,

        adminUid: String

    ): Result<Unit> {

        return subscriptionService
            .approveRequest(
                request,
                adminUid
            )
    }

    /**
     * Reject request
     */
    suspend fun rejectRequest(

        requestId: String,

        adminUid: String,

        reason: String

    ): Result<Unit> {

        return subscriptionService
            .rejectRequest(
                requestId,
                adminUid,
                reason
            )
    }
}
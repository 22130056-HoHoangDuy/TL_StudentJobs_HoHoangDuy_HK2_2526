package com.studentjobs.app.feature.subscription

import com.studentjobs.app.data.model.subscription.SubscriptionRequest
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole

data class SubscriptionUiState(

    // ====================================
    // SYSTEM
    // ====================================

    val isLoading: Boolean = false,

    val successMessage: String? = null,

    val errorMessage: String? = null,

    // ====================================
    // USER
    // ====================================

    val currentRole: UserRole =
        UserRole.STUDENT,

    // ====================================
    // SUBSCRIPTION
    // ====================================

    val currentPlan: SubscriptionPlan =
        SubscriptionPlan.FREE,

    val subscriptionExpiredAt: Long? = null,

    // ====================================
    // REQUEST
    // ====================================

    val hasPendingRequest: Boolean = false,

    val latestPendingRequest:
    SubscriptionRequest? = null,

    val pendingRequests:
    List<SubscriptionRequest> = emptyList()
)
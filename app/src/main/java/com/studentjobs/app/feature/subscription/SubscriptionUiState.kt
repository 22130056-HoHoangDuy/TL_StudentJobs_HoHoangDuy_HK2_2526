package com.studentjobs.app.feature.subscription

import com.studentjobs.app.data.model.subscription.SubscriptionRequest
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole

data class SubscriptionUiState(

    val isLoading: Boolean = false,

    val currentPlan: SubscriptionPlan =
        SubscriptionPlan.FREE,

    val currentRole: UserRole =
        UserRole.STUDENT,

    val pendingRequests:
    List<SubscriptionRequest> = emptyList(),

    val successMessage: String? = null,

    val errorMessage: String? = null
)

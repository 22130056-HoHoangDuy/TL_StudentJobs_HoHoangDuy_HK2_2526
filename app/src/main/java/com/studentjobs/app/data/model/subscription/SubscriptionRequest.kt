package com.studentjobs.app.data.model.subscription

import com.studentjobs.app.data.model.user.SubscriptionPlan

data class SubscriptionRequest(

    // id
    val requestId: String = "",

    // id user request
    val userUid: String = "",

    // PLUS
    val requestedPlan: SubscriptionPlan = SubscriptionPlan.PLUS,

    // default plan time for PLUS
    val durationDays: Int = 30,

    // payment method
    val paymentMethod: String = "QR",

    val paymentProofUrl: String? = null,

    val paymentAmount: Int = 0,

    val paymentContent: String = "",

    // status
    val status: SubscriptionRequestStatus = SubscriptionRequestStatus.PENDING,

    // admin review
    val reviewedBy: String? = null,
    val reviewNote: String? = null,

    // system log
    val requestedAt: Long = 0L,
    val reviewedAt: Long? = null
)
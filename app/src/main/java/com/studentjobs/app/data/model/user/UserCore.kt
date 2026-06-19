package com.studentjobs.app.data.model.user

import java.util.Date

data class UserCore(

    // identity
    val uid: String = "",
    val role: UserRole = UserRole.STUDENT,

    // auth
    val loginEmail: String = "",
    val phoneNumber: String? = null,

    // default state of user before verified is FALSE
    val userVerified: Boolean = false,

    // status of user account
    val status: UserStatus = UserStatus.ACTIVE,

    // user trust score
    val trustScore: Int = 0,

    // subscription FREE / PLUS, default is FREE
    val subscriptionPlan: SubscriptionPlan = SubscriptionPlan.FREE,
    val subscriptionExpiredAt: Date? = null,

    // system log
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)
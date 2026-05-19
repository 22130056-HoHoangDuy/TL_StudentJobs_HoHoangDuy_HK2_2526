package com.studentjobs.app.data.model.user

import com.studentjobs.app.data.model.UserRole

data class UserCore(

    // ===== IDENTITY =====
    val uid: String = "",
    val role: com.studentjobs.app.data.model.user.UserRole = UserRole.STUDENT,

    // ===== AUTH =====
    val email: String = "",
    val phoneNumber: String? = null,

    // ===== VERIFY =====
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,

    // ===== ACCOUNT =====
    val status: UserStatus = UserStatus.ACTIVE,

    // ===== TRUST =====
    val trustScore: Int = 0,

    // ===== SUBSCRIPTION =====
    val subscriptionPlan: SubscriptionPlan =
        SubscriptionPlan.FREE,

    val subscriptionExpiredAt: Long? = null,

    // ===== SYSTEM =====
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
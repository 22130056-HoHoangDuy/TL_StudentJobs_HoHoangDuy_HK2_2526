package com.studentjobs.app.data.repository.user

import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.firebase.firestore.UserServiceNew
import java.util.Date

class UserRepository(

    private val userService: UserServiceNew

) {

    suspend fun getUserCore(
        uid: String
    ): UserCore? {

        return userService.getUserCore(uid)
    }

    suspend fun isPlusActive(
        uid: String
    ): Boolean {

        val user =
            getUserCore(uid)
                ?: return false

        val now = Date()

        return user.subscriptionPlan == SubscriptionPlan.PLUS &&
                (user.subscriptionExpiredAt?.after(now) == true)
    }
}
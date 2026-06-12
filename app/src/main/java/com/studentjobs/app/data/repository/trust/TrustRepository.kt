package com.studentjobs.app.data.repository.trust

import com.studentjobs.app.data.model.trust.TrustLog
import com.studentjobs.app.firebase.firestore.TrustService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import java.util.UUID

class TrustRepository(

    private val trustService: TrustService,

    private val userService: UserServiceNew

) {

    suspend fun getTrustScore(
        uid: String
    ): Int {

        return userService
            .getUserCore(uid)
            ?.trustScore
            ?: 0
    }

    suspend fun getTrustLogs(
        uid: String
    ): List<TrustLog> {

        return trustService
            .getTrustLogs(uid)
    }

    // ========================================
    // ADD TRUST EVENT
    // ========================================

    suspend fun addTrustEvent(

        uid: String,

        actionType: String,

        changeAmount: Int,

        description: String,

        severity: String = "LOW",

        uniqueEvent: Boolean = true

    ) {

        try {

            // ========================================
            // PREVENT DUPLICATE REWARD
            // ========================================

            if (uniqueEvent) {

                val existingLog =

                    trustService
                        .getTrustLogs(uid)
                        .firstOrNull {

                            it.actionType ==
                                    actionType
                        }

                if (existingLog != null) {

                    return
                }
            }

            // ========================================
            // CURRENT USER
            // ========================================

            val user =

                userService
                    .getUserCore(uid)
                    ?: return

            // ========================================
            // UPDATE TRUST SCORE
            // ========================================

            val newScore =

                (
                        user.trustScore +
                                changeAmount
                        )
                    .coerceIn(
                        0,
                        100
                    )

            userService
                .updateTrustScore(

                    uid = uid,

                    score = newScore
                )

            // ========================================
            // CREATE LOG
            // ========================================

            trustService
                .addTrustLog(

                    TrustLog(

                        trustLogId =
                            UUID.randomUUID()
                                .toString(),

                        userUid = uid,

                        actionType =
                            actionType,

                        changeAmount =
                            changeAmount,

                        severity =
                            severity,

                        description =
                            description,

                        createdAt =
                            System.currentTimeMillis()
                    )
                )

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}
package com.studentjobs.app.firebase.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.model.user.UserStatus
import kotlinx.coroutines.tasks.await

class UserServiceNew {

    private val db = FirebaseFirestore.getInstance()

    // =========================
    // CREATE USER CORE
    // =========================
    suspend fun createUserCore(
        user: UserCore
    ): Result<Unit> {

        return try {

            val userMap = mapOf(

                // ===== IDENTITY =====
                "uid" to user.uid,

                // ===== ROLE =====
                "role" to user.role.name,

                // ===== AUTH =====
                "email" to user.email,
                "phoneNumber" to user.phoneNumber,

                // ===== VERIFY =====
                "isEmailVerified" to user.isEmailVerified,
                "isPhoneVerified" to user.isPhoneVerified,

                // ===== TRUST =====
                "trustScore" to user.trustScore,

                // ===== SUBSCRIPTION =====
                "subscriptionPlan" to
                        user.subscriptionPlan.name,

                "subscriptionExpiredAt" to
                        user.subscriptionExpiredAt,

                // ===== STATUS =====
                "status" to user.status.name,

                // ===== SYSTEM =====
                "createdAt" to user.createdAt,
                "updatedAt" to user.updatedAt
            )

            db.collection("users")
                .document(user.uid)
                .set(userMap)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // =========================
    // GET USER CORE
    // =========================
    suspend fun getUserCore(
        uid: String
    ): UserCore? {

        return try {

            val doc = db.collection("users")
                .document(uid)
                .get()
                .await()

            if (!doc.exists()) {
                return null
            }

            UserCore(

                uid = doc.getString("uid") ?: "",

                role = try {
                    UserRole.valueOf(
                        doc.getString("role")
                            ?: "STUDENT"
                    )
                } catch (e: Exception) {
                    UserRole.STUDENT
                },

                email = doc.getString("email") ?: "",

                phoneNumber =
                    doc.getString("phoneNumber"),

                isEmailVerified =
                    doc.getBoolean("isEmailVerified")
                        ?: false,

                isPhoneVerified =
                    doc.getBoolean("isPhoneVerified")
                        ?: false,

                trustScore =
                    (doc.getLong("trustScore")
                        ?: 0L).toInt(),

                subscriptionPlan = try {
                    SubscriptionPlan.valueOf(
                        doc.getString("subscriptionPlan")
                            ?: "FREE"
                    )
                } catch (e: Exception) {
                    SubscriptionPlan.FREE
                },

                subscriptionExpiredAt =
                    doc.getLong("subscriptionExpiredAt"),

                status = try {
                    UserStatus.valueOf(
                        doc.getString("status")
                            ?: "ACTIVE"
                    )
                } catch (e: Exception) {
                    UserStatus.ACTIVE
                },

                createdAt =
                    doc.getLong("createdAt")
                        ?: 0L,

                updatedAt =
                    doc.getLong("updatedAt")
                        ?: 0L
            )

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // =========================
    // REALTIME LISTENER
    // =========================
    fun listenUserCore(

        uid: String,

        onChange: (UserCore) -> Unit

    ) {

        db.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {

                    Log.e(
                        "USER_CORE",
                        "Listen failed",
                        error
                    )

                    return@addSnapshotListener
                }

                if (snapshot == null ||
                    !snapshot.exists()
                ) {
                    return@addSnapshotListener
                }

                val user = UserCore(

                    uid =
                        snapshot.getString("uid") ?: "",

                    role = try {
                        UserRole.valueOf(
                            snapshot.getString("role")
                                ?: "STUDENT"
                        )
                    } catch (e: Exception) {
                        UserRole.STUDENT
                    },

                    email =
                        snapshot.getString("email")
                            ?: "",

                    phoneNumber =
                        snapshot.getString("phoneNumber"),

                    isEmailVerified =
                        snapshot.getBoolean(
                            "isEmailVerified"
                        ) ?: false,

                    isPhoneVerified =
                        snapshot.getBoolean(
                            "isPhoneVerified"
                        ) ?: false,

                    trustScore =
                        (snapshot.getLong(
                            "trustScore"
                        ) ?: 0L).toInt(),

                    subscriptionPlan = try {
                        SubscriptionPlan.valueOf(
                            snapshot.getString(
                                "subscriptionPlan"
                            ) ?: "FREE"
                        )
                    } catch (e: Exception) {
                        SubscriptionPlan.FREE
                    },

                    subscriptionExpiredAt =
                        snapshot.getLong(
                            "subscriptionExpiredAt"
                        ),

                    status = try {
                        UserStatus.valueOf(
                            snapshot.getString("status")
                                ?: "ACTIVE"
                        )
                    } catch (e: Exception) {
                        UserStatus.ACTIVE
                    },

                    createdAt =
                        snapshot.getLong("createdAt")
                            ?: 0L,

                    updatedAt =
                        snapshot.getLong("updatedAt")
                            ?: 0L
                )

                onChange(user)
            }
    }
}
package com.studentjobs.app.firebase.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.model.user.UserStatus
import kotlinx.coroutines.tasks.await
import java.util.Date

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

                "email" to user.loginEmail,

                "phoneNumber" to user.phoneNumber,

                // ===== VERIFY =====

                "userVerified" to user.userVerified,

                // ===== TRUST =====

                "trustScore" to user.trustScore,

                // ===== SUBSCRIPTION =====

                "subscriptionPlan" to user.subscriptionPlan.name,

                "subscriptionExpiredAt" to user.subscriptionExpiredAt,

                // ===== STATUS =====

                "status" to user.status.name,

                // ===== SYSTEM =====

                "createdAt" to user.createdAt,

                "updatedAt" to user.updatedAt
            )

            db.collection("users").document(user.uid).set(userMap).await()

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

            val doc = db.collection("users").document(uid).get().await()

            if (!doc.exists()) {
                return null
            }

            UserCore(

                uid = doc.getString("uid") ?: "",

                role = try {

                    UserRole.valueOf(

                        doc.getString("role") ?: "STUDENT"
                    )

                } catch (e: Exception) {

                    UserRole.STUDENT
                },

                loginEmail = doc.getString("email") ?: "",

                phoneNumber = doc.getString("phoneNumber"),

                userVerified = doc.getBoolean("userVerified") ?: false,

                trustScore = (doc.getLong("trustScore") ?: 0L).toInt(),

                subscriptionPlan = try {

                    SubscriptionPlan.valueOf(

                        doc.getString(
                            "subscriptionPlan"
                        ) ?: "FREE"
                    )

                } catch (e: Exception) {

                    SubscriptionPlan.FREE
                },

                subscriptionExpiredAt = doc.getDate(
                    "subscriptionExpiredAt"
                ),

                status = try {

                    UserStatus.valueOf(

                        doc.getString("status") ?: "ACTIVE"
                    )

                } catch (e: Exception) {

                    UserStatus.ACTIVE
                },

                createdAt = doc.getDate("createdAt"),

                updatedAt = doc.getDate("updatedAt")
            )

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // ========================================
    // UPDATE USER VERIFIED
    // ========================================

    suspend fun updateStudentVerificationStatus(

        uid: String,

        verified: Boolean

    ): Result<Unit> {

        return try {

            db.collection("users").document(uid).update(

                mapOf(

                    "userVerified" to verified,

                    "updatedAt" to Date(),
                )
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // LISTEN USER CORE
    // ========================================

    fun listenUserCore(

        uid: String,

        onChange: (UserCore?) -> Unit

    ) {

        db.collection("users").document(uid).addSnapshotListener { snapshot, _ ->

            val user =

                snapshot?.toObject(
                    UserCore::class.java
                )

            onChange(user)
        }
    }

    // ========================================
    // EMPLOYER VERIFICATION
    // ========================================

    suspend fun submitEmployerVerification(
        uid: String,
        businessName: String,
        businessCategory: String,
        businessAddress: String,
        businessDescription: String,
        businessLocationUrl: String,
        businessLatitude: Double?,
        businessLongitude: Double?,
        businessLicenseUrl: String,
        businessStoreFrontImageUrl: String
    ): Result<Unit> {
        return try {
            val now = Date()

            db.collection("employer_verifications").document(uid).set(
                mapOf(
                    "uid" to uid,
                    "businessName" to businessName,
                    "businessCategory" to businessCategory,
                    "businessDescription" to businessDescription,
                    "businessAddressText" to businessAddress,
                    "businessLocationUrl" to businessLocationUrl,
                    "businessLatitude" to businessLatitude,
                    "businessLongitude" to businessLongitude,
                    "businessLicenseUrl" to businessLicenseUrl,
                    "businessStoreFrontImageUrl" to businessStoreFrontImageUrl,
                    "submissionStatus" to "PENDING",
                    "submittedAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
                ), SetOptions.merge()
            ).await()

            db.collection("employers").document(uid).set(
                mapOf(
                    "uid" to uid,
                    "businessName" to businessName,
                    "businessCategory" to businessCategory,
                    "businessDescription" to businessDescription,
                    "businessAddressText" to businessAddress,
                    "businessLocationUrl" to businessLocationUrl,
                    "businessLatitude" to businessLatitude,
                    "businessLongitude" to businessLongitude,
                    "businessStoreFrontImageUrl" to businessStoreFrontImageUrl,
                    "updatedAt" to now
                ), SetOptions.merge()
            ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // UPDATE EMPLOYER VERIFIED
    suspend fun updateEmployerVerificationStatus(

        uid: String,

        verified: Boolean

    ): Result<Unit> {

        return try {

            db.collection("users").document(uid).set(

                mapOf(

                    "userVerified" to verified,

                    "updatedAt" to Date()

                ),

                SetOptions.merge()
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    suspend fun updateTrustScore(

        uid: String,

        score: Int

    ): Result<Unit> {

        return try {

            db.collection("users")
                .document(uid)
                .update(

                    mapOf(

                        "trustScore" to score,

                        "updatedAt" to Date()
                    )
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    suspend fun getUserByEmail(
        email: String
    ): UserCore? {

        return try {

            val snapshot =

                db.collection("users")
                    .whereEqualTo(
                        "email",
                        email
                    )
                    .limit(1)
                    .get()
                    .await()

            if (snapshot.isEmpty) {

                null

            } else {

                snapshot.documents.first()
                    .toObject(
                        UserCore::class.java
                    )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }
}

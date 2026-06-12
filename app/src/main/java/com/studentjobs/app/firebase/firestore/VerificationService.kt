package com.studentjobs.app.firebase.firestore

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.studentjobs.app.data.model.employer.EmployerVerification
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.student.StudentVerification
import kotlinx.coroutines.tasks.await

class VerificationService {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // ========================================
    // CREATE STUDENT VERIFICATION
    // ========================================
    suspend fun createStudentVerification(
        verification: StudentVerification
    ): Result<Unit> {

        return try {

            db.collection("student_verifications").document(verification.uid)
                .set(verification, SetOptions.merge())
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // GET STUDENT VERIFICATION
    // ========================================
    suspend fun getStudentVerification(
        uid: String
    ): StudentVerification? {

        return try {

            db.collection("student_verifications").document(uid).get().await()
                .toObject(StudentVerification::class.java)

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // ========================================
    // CREATE EMPLOYER VERIFICATION
    // ========================================
    suspend fun createEmployerVerification(
        verification: EmployerVerification
    ): Result<Unit> {

        return try {

            db.collection("employer_verifications").document(verification.uid)
                .set(verification, SetOptions.merge())
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // GET EMPLOYER VERIFICATION
    // ========================================
    suspend fun getEmployerVerification(
        uid: String
    ): EmployerVerification? {

        return try {

            db.collection("employer_verifications").document(uid).get().await()
                .toObject(EmployerVerification::class.java)

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    suspend fun updateStudentVerificationFields(
        uid: String,
        fields: Map<String, Any>
    ): Result<Unit> {

        return try {

            db.collection("student_verifications")
                .document(uid)
                .update(fields)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    suspend fun updateEmployerVerificationFields(

        uid: String,

        fields: Map<String, Any>

    ): Result<Unit> {

        return try {

            db.collection("employer_verifications")
                .document(uid)
                .update(fields)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    //
    suspend fun uploadStudentCardImage(

        uid: String,

        imageUri: Uri,

        isFront: Boolean

    ): Result<String> {

        return try {

            val fileName =

                if (isFront) "front.jpg"
                else "back.jpg"

            val ref = storage.reference.child(
                "student_cards/$uid/$fileName"
            )

            ref.putFile(imageUri).await()

            val downloadUrl = ref.downloadUrl.await()

            Result.success(
                downloadUrl.toString()
            )

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }

    }

    fun listenStudentVerification(
        uid: String, onChange: (StudentVerification?) -> Unit
    ) {

        db.collection("student_verifications").document(uid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {

                    error.printStackTrace()

                    onChange(null)

                    return@addSnapshotListener
                }

                if (snapshot == null || !snapshot.exists()) {

                    onChange(null)

                    return@addSnapshotListener
                }

                try {

                    val data = snapshot.data

                    val verification = StudentVerification(

                        uid = data?.get("uid") as? String ?: "",

                        extractedStudentName = data?.get(
                            "extractedStudentName"
                        ) as? String,

                        extractedStudentId = data?.get(
                            "extractedStudentId"
                        ) as? String,

                        extractedStudentSchoolName = data?.get(
                            "extractedStudentSchoolName"
                        ) as? String,

                        extractedStudentDob = data?.get(
                            "extractedStudentDob"
                        ) as? String,

                        extractedStudentMajor = data?.get(
                            "extractedStudentMajor"
                        ) as? String,

                        studentCardFrontUrl = data?.get(
                            "studentCardFrontUrl"
                        ) as? String,

                        studentCardBackUrl = data?.get(
                            "studentCardBackUrl"
                        ) as? String,

                        studentCardVerified = try {

                            VerificationStatus.valueOf(

                                data?.get(
                                    "studentCardVerified"
                                ) as? String ?: "UNVERIFIED"
                            )

                        } catch (e: Exception) {

                            VerificationStatus.UNVERIFIED
                        },

                        studentEmailVerified = try {

                            VerificationStatus.valueOf(

                                data?.get(
                                    "studentEmailVerified"
                                ) as? String ?: "UNVERIFIED"
                            )

                        } catch (e: Exception) {

                            VerificationStatus.UNVERIFIED
                        },

                        studentPhoneVerified = try {

                            VerificationStatus.valueOf(

                                data?.get(
                                    "studentPhoneVerified"
                                ) as? String ?: "UNVERIFIED"
                            )

                        } catch (e: Exception) {

                            VerificationStatus.UNVERIFIED
                        },

                        createdAt =
                            (data?.get("createdAt") as? Long)
                                ?: 0L,

                        updatedAt =
                            (data?.get("updatedAt") as? Long)
                                ?: 0L
                    )

                    onChange(verification)

                } catch (e: Exception) {

                    e.printStackTrace()

                    onChange(null)
                }
            }
    }

    fun listenEmployerVerification(

        uid: String,

        onChange: (EmployerVerification?) -> Unit

    ) {

        db.collection("employer_verifications")
            .document(uid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {

                    error.printStackTrace()

                    onChange(null)

                    return@addSnapshotListener
                }

                if (snapshot == null || !snapshot.exists()) {

                    onChange(null)

                    return@addSnapshotListener
                }

                try {

                    val data = snapshot.data

                    val verification = EmployerVerification(

                        uid =
                            data?.get("uid") as? String
                                ?: "",

                        businessLicenseUrl =
                            data?.get(
                                "businessLicenseUrl"
                            ) as? String,

                        businessName =
                            data?.get(
                                "businessName"
                            ) as? String,

                        businessCategory =
                            data?.get(
                                "businessCategory"
                            ) as? String,

                        businessDescription =
                            data?.get(
                                "businessDescription"
                            ) as? String,

                        businessLocationUrl =
                            data?.get(
                                "businessLocationUrl"
                            ) as? String,

                        businessAddressText =
                            data?.get(
                                "businessAddressText"
                            ) as? String,

                        businessLatitude =
                            data?.get(
                                "businessLatitude"
                            ) as? Double,

                        businessLongitude =
                            data?.get(
                                "businessLongitude"
                            ) as? Double,

                        businessStoreFrontImageUrl =
                            data?.get(
                                "businessStoreFrontImageUrl"
                            ) as? String,

                        businessLicenseVerified = try {

                            VerificationStatus.valueOf(

                                data?.get(
                                    "businessLicenseVerified"
                                ) as? String
                                    ?: "UNVERIFIED"
                            )

                        } catch (e: Exception) {

                            VerificationStatus.UNVERIFIED
                        },

                        businessEmailVerified = try {

                            VerificationStatus.valueOf(

                                data?.get(
                                    "businessEmailVerified"
                                ) as? String
                                    ?: "UNVERIFIED"
                            )

                        } catch (e: Exception) {

                            VerificationStatus.UNVERIFIED
                        },

                        businessPhoneVerified = try {

                            VerificationStatus.valueOf(

                                data?.get(
                                    "businessPhoneVerified"
                                ) as? String
                                    ?: "UNVERIFIED"
                            )

                        } catch (e: Exception) {

                            VerificationStatus.UNVERIFIED
                        },

                        submissionStatus = try {

                            VerificationStatus.valueOf(

                                data?.get(
                                    "submissionStatus"
                                ) as? String
                                    ?: "UNVERIFIED"
                            )

                        } catch (e: Exception) {

                            VerificationStatus.UNVERIFIED
                        },

                        rejectionReason =
                            data?.get(
                                "rejectionReason"
                            ) as? String,

                        submittedAt =
                            (data?.get(
                                "submittedAt"
                            ) as? Long) ?: 0L,

                        reviewedAt =
                            data?.get(
                                "reviewedAt"
                            ) as? Long,

                        reviewedBy =
                            data?.get(
                                "reviewedBy"
                            ) as? String
                    )

                    onChange(verification)

                } catch (e: Exception) {

                    e.printStackTrace()

                    onChange(null)
                }
            }
    }
}

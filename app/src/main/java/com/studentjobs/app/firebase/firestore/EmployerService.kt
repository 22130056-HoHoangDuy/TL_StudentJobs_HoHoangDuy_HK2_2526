package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.employer.EmployerProfile
import kotlinx.coroutines.tasks.await

class EmployerService {

    private val db = FirebaseFirestore.getInstance()

    // =========================
    // CREATE EMPLOYER PROFILE
    // =========================
    suspend fun createEmployerProfile(
        employer: EmployerProfile
    ): Result<Unit> {

        return try {

            db.collection("employers")
                .document(employer.uid)
                .set(employer)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // =========================
    // GET EMPLOYER PROFILE
    // =========================
    suspend fun getEmployerProfile(
        uid: String
    ): EmployerProfile? {

        return try {

            db.collection("employers")
                .document(uid)
                .get()
                .await()
                .toObject(EmployerProfile::class.java)

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // =========================
    // UPDATE EMPLOYER PROFILE
    // =========================
    suspend fun updateEmployerProfile(
        employer: EmployerProfile
    ): Result<Unit> {

        return try {

            db.collection("employers")
                .document(employer.uid)
                .set(employer)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // Trong EmployerService.kt
    fun listenEmployerProfile(
        uid: String,
        onChange: (EmployerProfile?) -> Unit
    ) {
        db.collection("employers")
            .document(uid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    android.util.Log.e(
                        "EMPLOYER_DEBUG",
                        "Listener Error",
                        error
                    )
                    return@addSnapshotListener
                }

                android.util.Log.e(
                    "EMPLOYER_DEBUG",
                    "SNAPSHOT=${snapshot?.data}"
                )

                try {

                    val profile =
                        snapshot?.toObject(
                            EmployerProfile::class.java
                        )

                    android.util.Log.e(
                        "EMPLOYER_DEBUG",
                        "PROFILE=$profile"
                    )

                    onChange(profile)

                } catch (e: Exception) {

                    android.util.Log.e(
                        "EMPLOYER_DEBUG",
                        "DESERIALIZE_ERROR",
                        e
                    )

                    onChange(null)
                }
            }
    }
}
package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.application.ApplicationEntity
import com.studentjobs.app.data.model.application.ApplicationStatus
import kotlinx.coroutines.tasks.await

class ApplicationService {

    private val db = FirebaseFirestore.getInstance()

    companion object {

        private const val COLLECTION = "applications"
    }

    suspend fun createApplication(
        application: ApplicationEntity
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION).document(
                application.applicationId
            ).set(application).await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    suspend fun getApplicationsByStudent(
        studentUid: String
    ): List<ApplicationEntity> {

        return try {

            db.collection(COLLECTION)

                .whereEqualTo(
                    "studentUid", studentUid
                )

                .get().await()

                .toObjects(
                    ApplicationEntity::class.java
                )

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun getApplicationsByJob(
        jobId: String
    ): List<ApplicationEntity> {

        return try {

            db.collection(COLLECTION)

                .whereEqualTo(
                    "jobId", jobId
                )

                .get().await()

                .toObjects(
                    ApplicationEntity::class.java
                )

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    // isApply ? "You have applied!" : "Accept application"
    suspend fun hasApplied(

        studentUid: String,

        jobId: String

    ): Boolean {

        return try {

            val snapshot =

                db.collection(COLLECTION)

                    .whereEqualTo(
                        "studentUid", studentUid
                    )

                    .whereEqualTo(
                        "jobId", jobId
                    )

                    .get().await()

            !snapshot.isEmpty

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    suspend fun updateStatus(

        applicationId: String,

        status: String

    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)

                .document(
                    applicationId
                )

                .update(
                    "status", status
                )

                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }

    }

    suspend fun getActiveApplicationsByStudent(
        studentUid: String
    ): List<ApplicationEntity> {

        val applications =

            getApplicationsByStudent(
                studentUid
            )

        return applications.filter {

            it.status ==
                    ApplicationStatus.PENDING.name

                    ||

                    it.status ==
                    ApplicationStatus.ACCEPTED.name

                    ||

                    it.status ==
                    ApplicationStatus.WORKING.name
        }
    }

}

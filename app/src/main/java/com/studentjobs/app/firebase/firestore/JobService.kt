package com.studentjobs.app.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.job.JobEntity
import kotlinx.coroutines.tasks.await

class JobService {

    private val db =
        FirebaseFirestore.getInstance()

    companion object {

        private const val COLLECTION =
            "jobs"
    }

    // ========================================
    // CREATE JOB
    // ========================================

    suspend fun createJob(
        job: JobEntity
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)
                .document(job.jobId)
                .set(job)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // GET JOB
    // ========================================

    suspend fun getJob(
        jobId: String
    ): JobEntity? {

        return try {

            db.collection(COLLECTION)
                .document(jobId)
                .get()
                .await()
                .toObject(JobEntity::class.java)

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // ========================================
    // GET ALL JOBS
    // ========================================

    suspend fun getAllJobs():
            List<JobEntity> {

        return try {

            db.collection(COLLECTION)
                .get()
                .await()
                .toObjects(
                    JobEntity::class.java
                )

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    // ========================================
    // GET JOBS BY EMPLOYER
    // ========================================

    suspend fun getJobsByEmployer(
        employerUid: String
    ): List<JobEntity> {

        return try {

            db.collection(COLLECTION)

                .whereEqualTo(
                    "employerUid",
                    employerUid
                )

                .get()
                .await()

                .toObjects(
                    JobEntity::class.java
                )

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    // ========================================
    // UPDATE JOB
    // ========================================

    suspend fun updateJob(
        job: JobEntity
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)
                .document(job.jobId)
                .set(job)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }

    // ========================================
    // DELETE JOB
    // ========================================

    suspend fun deleteJob(
        jobId: String
    ): Result<Unit> {

        return try {

            db.collection(COLLECTION)
                .document(jobId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }
    // ========================================
// GET ACTIVE JOBS
// ========================================

    suspend fun getActiveJobs():
            List<JobEntity> {

        return try {

            db.collection(COLLECTION)

                .whereEqualTo(
                    "status",
                    "ACTIVE"
                )

                .whereEqualTo(
                    "moderationStatus",
                    "APPROVED"
                )

                .get()
                .await()

                .toObjects(
                    JobEntity::class.java
                )

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }
}

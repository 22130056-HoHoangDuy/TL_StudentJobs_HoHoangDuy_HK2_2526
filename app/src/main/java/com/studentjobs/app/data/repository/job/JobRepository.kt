package com.studentjobs.app.data.repository.job

import com.studentjobs.app.data.model.job.JobEntity
import com.studentjobs.app.data.model.job.ShiftEntity
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.JobService
import com.studentjobs.app.firebase.firestore.ShiftService

class JobRepository(

    private val jobService: JobService,

    private val shiftService: ShiftService,

    private val employerService: EmployerService

) {

    // ========================================
    // CREATE JOB + SHIFTS
    // ========================================

    suspend fun createJob(

        job: JobEntity,

        shifts: List<ShiftEntity>

    ): Result<Unit> {

        return try {

            val employer =

                employerService
                    .getEmployerProfile(
                        job.employerUid
                    )

                    ?: return Result.failure(

                        Exception(
                            "Employer profile not found"
                        )
                    )

            val completedJob =

                job.copy(

                    locationText =
                        employer.businessAddressText
                            ?: "",

                    latitude =
                        employer.businessLatitude,

                    longitude =
                        employer.businessLongitude
                )

            jobService
                .createJob(
                    completedJob
                )
                .getOrThrow()

            shiftService
                .createShifts(
                    shifts
                )
                .getOrThrow()

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

        return jobService
            .getJob(jobId)
    }

    // ========================================
    // GET ACTIVE JOBS
    // ========================================

    suspend fun getActiveJobs():
            List<JobEntity> {

        return jobService
            .getActiveJobs()
    }

    // ========================================
    // GET EMPLOYER JOBS
    // ========================================

    suspend fun getJobsByEmployer(
        employerUid: String
    ): List<JobEntity> {

        return jobService
            .getJobsByEmployer(
                employerUid
            )
    }

    // ========================================
    // GET SHIFTS
    // ========================================

    suspend fun getShiftsByJob(
        jobId: String
    ): List<ShiftEntity> {

        return shiftService
            .getShiftsByJob(
                jobId
            )
    }

    // ========================================
    // DELETE JOB
    // ========================================

    suspend fun deleteJob(
        jobId: String
    ): Result<Unit> {

        return try {

            shiftService
                .deleteShiftsByJob(
                    jobId
                )
                .getOrThrow()

            jobService
                .deleteJob(
                    jobId
                )
                .getOrThrow()

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()

            Result.failure(e)
        }
    }
}
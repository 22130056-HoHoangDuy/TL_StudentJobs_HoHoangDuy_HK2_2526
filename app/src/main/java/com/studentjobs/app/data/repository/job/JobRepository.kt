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
    // SUGGESTED JOBS (MỚI)
    // ========================================

    suspend fun getSuggestedJobs(studentSkills: List<String>): List<JobEntity> {
        // Lấy tất cả job đang active
        val allJobs = jobService.getActiveJobs()

        // Lọc các job mà yêu cầu kỹ năng khớp với kỹ năng sinh viên
        // .any { it in studentSkills } kiểm tra nếu job yêu cầu ít nhất 1 kỹ năng sinh viên có
        return allJobs.filter { job ->
            job.requiredSkills.any { it in studentSkills }
        }.sortedByDescending { job ->
            // Sắp xếp ưu tiên job khớp nhiều kỹ năng nhất
            job.requiredSkills.count { it in studentSkills }
        }
    }

    // ========================================
    // CREATE JOB + SHIFTS
    // ========================================

    suspend fun createJob(
        job: JobEntity,
        shifts: List<ShiftEntity>
    ): Result<Unit> {
        return try {
            val employer = employerService.getEmployerProfile(job.employerUid)
                ?: return Result.failure(Exception("Employer profile not found"))

            val completedJob = job.copy(
                businessName = employer.businessName,
                businessCategory = employer.businessCategory ?: "",
                locationText = employer.businessAddressText ?: "",
                latitude = employer.businessLatitude,
                longitude = employer.businessLongitude
            )

            jobService.createJob(completedJob).getOrThrow()
            shiftService.createShifts(shifts).getOrThrow()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // ========================================
    // GETTERS
    // ========================================

    suspend fun getEmployerCategory(employerUid: String): String? {
        return employerService.getEmployerProfile(employerUid)?.businessCategory
    }

    suspend fun getEmployerProfile(employerUid: String) =
        employerService.getEmployerProfile(employerUid)

    suspend fun getJob(jobId: String): JobEntity? = jobService.getJob(jobId)

    suspend fun getActiveJobs(): List<JobEntity> = jobService.getActiveJobs()

    suspend fun getJobsByEmployer(employerUid: String): List<JobEntity> =
        jobService.getJobsByEmployer(employerUid)

    suspend fun getShiftsByJob(jobId: String): List<ShiftEntity> =
        shiftService.getShiftsByJob(jobId)

    // ========================================
    // DELETE JOB
    // ========================================

    suspend fun deleteJob(jobId: String): Result<Unit> {
        return try {
            shiftService.deleteShiftsByJob(jobId).getOrThrow()
            jobService.deleteJob(jobId).getOrThrow()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getJobWithShifts(jobId: String): Pair<JobEntity?, List<ShiftEntity>> {
        val job = jobService.getJob(jobId)
        val shifts = shiftService.getShiftsByJob(jobId)
        return Pair(job, shifts)
    }

    // ========================================
    // APPLICATIONS
    // ========================================

    suspend fun incrementApplicantCount(jobId: String): Result<Unit> =
        jobService.incrementApplicantCount(jobId)

    suspend fun decrementApplicantCount(jobId: String): Result<Unit> =
        jobService.decrementApplicantCount(jobId)

    suspend fun incrementAcceptedApplicantCount(jobId: String): Result<Unit> =
        jobService.incrementAcceptedApplicantCount(jobId)

    // ========================================
    // UPDATE JOB
    // ========================================

    suspend fun updateJob(job: JobEntity): Result<Unit> = jobService.updateJob(job)

    // Thêm vào JobRepository.kt
    suspend fun getAllCategories(): List<String> {
        val jobs = jobService.getActiveJobs()
        // Lấy ra danh sách các category duy nhất, không trùng lặp
        return jobs.map { it.businessCategory }
            .filter { it.isNotEmpty() }
            .distinct()
            .sorted()
    }

    fun listenJobsByEmployer(
        employerUid: String,
        onChange: (List<JobEntity>) -> Unit
    ) {

        jobService.listenJobsByEmployer(
            employerUid,
            onChange
        )
    }
}
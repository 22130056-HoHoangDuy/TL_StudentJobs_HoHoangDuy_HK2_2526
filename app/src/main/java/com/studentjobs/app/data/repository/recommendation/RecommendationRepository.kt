package com.studentjobs.app.data.repository.recommendation

import com.studentjobs.app.data.model.job.JobEntity
import com.studentjobs.app.data.model.student.StudentSchedule
import com.studentjobs.app.feature.recommendation.RecommendationEngine
import com.studentjobs.app.feature.recommendation.RecommendationFilter
import com.studentjobs.app.feature.recommendation.RecommendedJob
import com.studentjobs.app.feature.recommendation.ScheduleConflictUtils
import com.studentjobs.app.firebase.firestore.JobService
import com.studentjobs.app.firebase.firestore.ShiftService
import com.studentjobs.app.firebase.firestore.StudentScheduleService
import com.studentjobs.app.firebase.firestore.StudentService
import com.studentjobs.app.firebase.firestore.UserServiceNew

class RecommendationRepository {

    private val studentService =
        StudentService()

    private val scheduleService =
        StudentScheduleService()

    private val jobService =
        JobService()

    private val shiftService =
        ShiftService()

    private val userService =
        UserServiceNew()

    suspend fun getRecommendations(

        uid: String

    ): List<RecommendedJob> {

        val student =

            studentService
                .getStudentProfile(uid)

                ?: return emptyList()

        val schedule =

            scheduleService
                .getSchedule(uid)

                ?: StudentSchedule()

        val activeJobs =

            jobService
                .getActiveJobs()

        val filteredJobs =

            mutableListOf<JobEntity>()

        for (job in activeJobs) {

            // =====================
            // VACANCY
            // =====================

            if (
                !RecommendationFilter
                    .hasVacancy(job)
            ) {

                continue
            }

            // =====================
            // DISTANCE
            // =====================

            val distancePass =

                RecommendationFilter
                    .filterJobs(
                        student,
                        listOf(job)
                    )
                    .isNotEmpty()

            if (!distancePass) {

                continue
            }

            // =====================
            // CATEGORY
            // =====================

            if (

                !RecommendationFilter
                    .categoryMatched(
                        student,
                        job
                    )

            ) {

                continue
            }

            // =====================
            // SCHEDULE
            // =====================

            val shifts =

                shiftService
                    .getShiftsByJob(
                        job.jobId
                    )

            val conflict =

                ScheduleConflictUtils
                    .hasConflict(

                        schedule.busySlots,

                        shifts
                    )

            if (conflict) {

                continue
            }

            filteredJobs.add(job)
        }

        val jobsWithTrust =

            mutableListOf<
                    Pair<JobEntity, Int>
                    >()

        for (job in filteredJobs) {

            val trustScore =

                userService
                    .getUserCore(
                        job.employerUid
                    )
                    ?.trustScore

                    ?: 0

            jobsWithTrust.add(

                Pair(
                    job,
                    trustScore
                )
            )
        }

        return RecommendationEngine
            .recommendJobs(

                student = student,

                jobs = jobsWithTrust
            )
    }
}
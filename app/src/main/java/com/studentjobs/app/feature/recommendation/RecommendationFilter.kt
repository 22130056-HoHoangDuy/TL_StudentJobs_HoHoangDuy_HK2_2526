package com.studentjobs.app.feature.recommendation

import com.studentjobs.app.data.model.job.JobEntity
import com.studentjobs.app.data.model.student.StudentProfile

object RecommendationFilter {

    fun filterJobs(

        student: StudentProfile,

        jobs: List<JobEntity>

    ): List<JobEntity> {

        return jobs.filter { job ->

            isActive(job) &&

                    hasVacancy(job) &&

                    withinDistance(
                        student,
                        job
                    )
        }
    }

    private fun isActive(

        job: JobEntity

    ): Boolean {

        return job.status == "ACTIVE"
    }

    private fun withinDistance(

        student: StudentProfile,

        job: JobEntity

    ): Boolean {

        val studentLat =
            student.studentLatitude

        val studentLng =
            student.studentLongitude

        val jobLat =
            job.latitude

        val jobLng =
            job.longitude

        if (

            studentLat == null ||

            studentLng == null ||

            jobLat == null ||

            jobLng == null

        ) {

            return false
        }

        val distanceKm =

            DistanceUtils
                .calculateDistanceKm(

                    studentLat,
                    studentLng,

                    jobLat,
                    jobLng
                )

        return distanceKm <= 20
    }

    fun categoryMatched(

        student: StudentProfile,

        job: JobEntity

    ): Boolean {

        if (
            student.preferredJobCategories
                .isEmpty()
        ) {

            return true
        }

        return student
            .preferredJobCategories
            .any {

                it.equals(
                    job.businessCategory,
                    ignoreCase = true
                )
            }
    }

    fun hasVacancy(

        job: JobEntity

    ): Boolean {

        return job.currentApplicants <

                job.requiredApplicants
    }
    fun getDistanceKm(

        student: StudentProfile,

        job: JobEntity

    ): Double? {

        val studentLat =
            student.studentLatitude

        val studentLng =
            student.studentLongitude

        val jobLat =
            job.latitude

        val jobLng =
            job.longitude

        if (

            studentLat == null ||

            studentLng == null ||

            jobLat == null ||

            jobLng == null

        ) {

            return null
        }

        return DistanceUtils
            .calculateDistanceKm(

                studentLat,
                studentLng,

                jobLat,
                jobLng
            )
    }
}
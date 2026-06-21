package com.studentjobs.app.feature.recommendation

import com.studentjobs.app.data.model.job.JobEntity
import com.studentjobs.app.data.model.student.StudentProfile

object RecommendationEngine {

    fun calculateScore(

        student: StudentProfile,

        job: JobEntity,

        employerTrustScore: Int

    ): RecommendationResult {

        val skillScore =

            calculateSkillScore(
                student,
                job
            )

        val distanceScore =

            calculateDistanceScore(
                student,
                job
            )

        val trustScore =

            employerTrustScore
                .coerceIn(0, 100)
                .toDouble()

        val finalScore =

            skillScore * 0.5 +

                    distanceScore * 0.3 +

                    trustScore * 0.2

        return RecommendationResult(

            score = finalScore,

            skillScore = skillScore,

            distanceScore = distanceScore,

            trustScore = trustScore
        )
    }

    private fun calculateSkillScore(

        student: StudentProfile,

        job: JobEntity

    ): Double {

        if (
            job.requiredSkills.isEmpty()
        ) {

            return 100.0
        }

        val matched =

            job.requiredSkills.count {

                    requiredSkill ->

                student.skills.any {

                        studentSkill ->

                    studentSkill.equals(
                        requiredSkill,
                        ignoreCase = true
                    )
                }
            }

        return (

                matched.toDouble()

                        /

                        job.requiredSkills.size

                ) * 100.0
    }

    private fun calculateDistanceScore(

        student: StudentProfile,

        job: JobEntity

    ): Double {

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

            return 0.0
        }

        val distanceKm =

            DistanceUtils
                .calculateDistanceKm(

                    studentLat,
                    studentLng,

                    jobLat,
                    jobLng
                )

        return when {

            distanceKm <= 3.0 -> 100.0

            distanceKm <= 8.0 -> {

                100.0 -

                        ((distanceKm - 3.0) / 5.0) * 30.0
            }

            distanceKm <= 15.0 -> {

                70.0 -

                        ((distanceKm - 8.0) / 7.0) * 30.0
            }

            distanceKm <= 20.0 -> {

                40.0 -

                        ((distanceKm - 15.0) / 5.0) * 30.0
            }

            else -> 0.0
        }
    }

    fun recommendJobs(

        student: StudentProfile,

        jobs: List<Pair<JobEntity, Int>>

    ): List<RecommendedJob> {

        return jobs

            .map { pair ->

                val job =
                    pair.first

                val trustScore =
                    pair.second

                val result =

                    calculateScore(

                        student,

                        job,

                        trustScore
                    )

                RecommendedJob(

                    job = job,

                    recommendation = result,

                    employerTrustScore =
                        trustScore
                )
            }

            .sortedByDescending {

                it.recommendation.score
            }

            .take(10)
    }
}
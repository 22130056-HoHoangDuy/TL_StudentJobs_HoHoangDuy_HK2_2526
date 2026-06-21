package com.studentjobs.app.feature.recommendation

import com.studentjobs.app.data.model.job.JobEntity

data class RecommendedJob(

    val job: JobEntity,

    val recommendation:
    RecommendationResult,

    val employerTrustScore: Int = 0
)
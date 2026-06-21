package com.studentjobs.app.feature.recommendation

data class RecommendationResult(

    val score: Double = 0.0,

    val skillScore: Double = 0.0,

    val distanceScore: Double = 0.0,

    val trustScore: Double = 0.0
)
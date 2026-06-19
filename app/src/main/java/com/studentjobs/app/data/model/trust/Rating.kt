package com.studentjobs.app.data.model.trust

import java.util.Date

data class Rating(

    // ===== ID =====
    val ratingId: String = "",

    // ===== RELATION =====
    val fromUserUid: String = "",
    val toUserUid: String = "",

    val jobId: String = "",

    // ===== RATING =====
    val score: Int = 0,

    val comment: String? = null,

    // ===== SYSTEM =====
    val createdAt: Date? = null
)
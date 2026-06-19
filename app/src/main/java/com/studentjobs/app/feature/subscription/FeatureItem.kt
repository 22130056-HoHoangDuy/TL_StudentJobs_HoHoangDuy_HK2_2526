package com.studentjobs.app.feature.subscription

data class FeatureItem(

    val title: String,

    val description: String,

    val isAvailableInFree: Boolean = false,

    val isAvailableInPlus: Boolean = true
)
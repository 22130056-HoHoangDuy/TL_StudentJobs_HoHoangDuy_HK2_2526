package com.studentjobs.app.data.datasource

import com.studentjobs.app.data.model.job.JobEntity

val fakeJobs = listOf(

    JobEntity(

        // ===== ID =====
        jobId = "1",

        // ===== OWNER =====
        employerUid = "EMP_001",

        // ===== BASIC =====
        title = "Barista",

        description =
            "Part-time barista for coffee shop.",

        category = "Food & Beverage",

        // ===== SALARY =====
        salaryMin = 18000.0,

        salaryMax = 22000.0,

        // ===== LOCATION =====
        locationText = "Ho Chi Minh City",

        latitude = 10.7769,

        longitude = 106.7009,

        // ===== REQUIREMENTS =====
        requiredSkills = listOf(
            "Communication",
            "Teamwork"
        ),

        // ===== RECRUITMENT =====
        autoRecruitmentEnabled = true,

        // ===== MODERATION =====
        moderationStatus = "APPROVED",

        // ===== STATUS =====
        status = "ACTIVE",

        // ===== SYSTEM =====
        createdAt = System.currentTimeMillis(),

        updatedAt = System.currentTimeMillis()
    ),

    JobEntity(

        jobId = "2",

        employerUid = "EMP_002",

        title = "Shop Assistant",

        description =
            "Support customers and manage products.",

        category = "Retail",

        salaryMin = 17000.0,

        salaryMax = 20000.0,

        locationText = "Thu Duc City",

        latitude = 10.8500,

        longitude = 106.7710,

        requiredSkills = listOf(
            "Sales",
            "Communication"
        ),

        autoRecruitmentEnabled = false,

        moderationStatus = "APPROVED",

        status = "ACTIVE",

        createdAt = System.currentTimeMillis(),

        updatedAt = System.currentTimeMillis()
    ),

    JobEntity(

        jobId = "3",

        employerUid = "EMP_003",

        title = "Waiter",

        description =
            "Serve food and support restaurant operations.",

        category = "Restaurant",

        salaryMin = 20000.0,

        salaryMax = 25000.0,

        locationText = "District 1",

        latitude = 10.7756,

        longitude = 106.7019,

        requiredSkills = listOf(
            "Fast Service",
            "Friendly"
        ),

        autoRecruitmentEnabled = true,

        moderationStatus = "APPROVED",

        status = "ACTIVE",

        createdAt = System.currentTimeMillis(),

        updatedAt = System.currentTimeMillis()
    )
)
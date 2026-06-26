package com.studentjobs.app.data.model.notification

import java.util.Date

data class NotificationEntity(

    val notificationId: String = "",

    val receiverUid: String = "",

    val title: String = "",

    val message: String = "",

    val type: String = "",

    val relatedId: String = "",

    val isRead: Boolean = false,

    val createdAt: Date? = null
)
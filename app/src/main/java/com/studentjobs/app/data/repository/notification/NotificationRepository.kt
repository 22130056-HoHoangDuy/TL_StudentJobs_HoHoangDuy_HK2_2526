package com.studentjobs.app.data.repository.notification

import com.studentjobs.app.data.model.notification.NotificationEntity
import com.studentjobs.app.firebase.firestore.NotificationService

class NotificationRepository(

    private val service: NotificationService

) {

    suspend fun createNotification(

        notification: NotificationEntity

    ) =

        service.createNotification(
            notification
        )

    suspend fun getNotifications(

        receiverUid: String

    ) =

        service.getNotifications(
            receiverUid
        )

    suspend fun markAsRead(

        notificationId: String

    ) =

        service.markAsRead(
            notificationId
        )
}
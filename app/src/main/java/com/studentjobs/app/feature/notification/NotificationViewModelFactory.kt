package com.studentjobs.app.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.notification.NotificationRepository

class NotificationViewModelFactory(

    private val repository: NotificationRepository

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(

        modelClass: Class<T>

    ): T {

        return NotificationViewModel(

            repository

        ) as T
    }
}
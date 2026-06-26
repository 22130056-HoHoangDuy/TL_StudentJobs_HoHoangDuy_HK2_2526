package com.studentjobs.app.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.notification.NotificationEntity
import com.studentjobs.app.data.repository.notification.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(

    private val repository: NotificationRepository

) : ViewModel() {

    private val _notifications =

        MutableStateFlow<List<NotificationEntity>>(emptyList())

    val notifications:
            StateFlow<List<NotificationEntity>> =
        _notifications.asStateFlow()

    private val _isLoading =

        MutableStateFlow(false)

    val isLoading:
            StateFlow<Boolean> =
        _isLoading.asStateFlow()

    init {

        loadNotifications()
    }

    fun loadNotifications() {

        viewModelScope.launch {

            _isLoading.value = true

            val uid =

                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid

            if (uid == null) {

                _notifications.value = emptyList()

                _isLoading.value = false

                return@launch
            }

            _notifications.value =

                repository.getNotifications(uid)

            _isLoading.value = false
        }
    }

    fun markAsRead(

        notificationId: String

    ) {

        viewModelScope.launch {

            repository.markAsRead(
                notificationId
            )

            loadNotifications()
        }
    }
}
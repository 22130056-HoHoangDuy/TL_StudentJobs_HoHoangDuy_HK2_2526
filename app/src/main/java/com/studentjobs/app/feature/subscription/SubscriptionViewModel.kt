package com.studentjobs.app.feature.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.model.subscription.SubscriptionRequest
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.repository.subscription.SubscriptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(

    private val repository: SubscriptionRepository =
        SubscriptionRepository()

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SubscriptionUiState())

    val uiState: StateFlow<SubscriptionUiState> =
        _uiState.asStateFlow()

    fun setRole(role: UserRole) {

        _uiState.value = _uiState.value.copy(
            currentRole = role
        )
    }

    fun setCurrentPlan(plan: SubscriptionPlan) {

        _uiState.value = _uiState.value.copy(
            currentPlan = plan
        )
    }

    fun createSubscriptionRequest(

        request: SubscriptionRequest

    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            repository
                .createSubscriptionRequest(request)
                .onSuccess {

                    _uiState.value =
                        _uiState.value.copy(

                            isLoading = false,

                            successMessage =
                                "Đã gửi yêu cầu nâng cấp PLUS"
                        )
                }
                .onFailure {

                    _uiState.value =
                        _uiState.value.copy(

                            isLoading = false,

                            errorMessage =
                                it.message
                        )
                }
        }
    }

    fun loadPendingRequests() {

        viewModelScope.launch {

            repository
                .getPendingRequests()
                .onSuccess {

                    _uiState.value =
                        _uiState.value.copy(

                            pendingRequests = it
                        )
                }
        }
    }
}
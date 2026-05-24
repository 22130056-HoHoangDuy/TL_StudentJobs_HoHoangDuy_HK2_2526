package com.studentjobs.app.feature.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.model.subscription.SubscriptionRequest
import com.studentjobs.app.data.model.subscription.SubscriptionRequestStatus
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.repository.subscription.SubscriptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(

    private val repository: SubscriptionRepository = SubscriptionRepository()

) : ViewModel() {

    // ====================================
    // UI STATE
    // ====================================

    private val _uiState = MutableStateFlow(
        SubscriptionUiState()
    )

    val uiState: StateFlow<SubscriptionUiState> = _uiState.asStateFlow()

    // ====================================
    // ROLE
    // ====================================

    fun setRole(role: UserRole) {

        _uiState.value = _uiState.value.copy(

            currentRole = role
        )
    }

    // ====================================
    // PLAN
    // ====================================

    fun setCurrentPlan(plan: SubscriptionPlan) {

        _uiState.value = _uiState.value.copy(

            currentPlan = plan
        )
    }

    // ====================================
    // CREATE REQUEST
    // ====================================

    fun createSubscriptionRequest(

        request: SubscriptionRequest

    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(

                isLoading = true,

                errorMessage = null,

                successMessage = null
            )

            repository.createSubscriptionRequest(request)

                .onSuccess {

                    _uiState.value = _uiState.value.copy(

                        isLoading = false,

                        hasPendingRequest = true,

                        latestPendingRequest = request,

                        successMessage = "Đã gửi yêu cầu nâng cấp PLUS"
                    )
                }

                .onFailure {

                    _uiState.value = _uiState.value.copy(

                        isLoading = false,

                        errorMessage = it.message
                    )
                }
        }
    }

    // ====================================
    // LOAD PENDING REQUESTS
    // ====================================

    fun loadPendingRequests() {

        viewModelScope.launch {

            repository.getPendingRequests()

                .onSuccess { requests ->

                    _uiState.value = _uiState.value.copy(

                        pendingRequests = requests
                    )
                }
        }
    }

    // ====================================
    // CHECK USER PENDING REQUEST
    // ====================================

    fun checkPendingRequest(

        userUid: String

    ) {

        viewModelScope.launch {

            repository.getPendingRequests()

                .onSuccess { requests ->

                    val pendingRequest =

                        requests.firstOrNull {

                            it.userUid == userUid &&

                                    it.status == SubscriptionRequestStatus.PENDING
                        }

                    _uiState.value = _uiState.value.copy(

                        hasPendingRequest = pendingRequest != null,

                        latestPendingRequest = pendingRequest
                    )
                }
        }
    }

    // ====================================
    // UPDATE PLUS STATUS
    // ====================================

    fun updatePlusStatus(

        plan: SubscriptionPlan,

        expiredAt: Long?

    ) {

        _uiState.value = _uiState.value.copy(

            currentPlan = plan,

            subscriptionExpiredAt = expiredAt
        )
    }

    // ====================================
    // CLEAR MESSAGE
    // ====================================

    fun clearMessages() {

        _uiState.value = _uiState.value.copy(

            successMessage = null,

            errorMessage = null
        )
    }
}
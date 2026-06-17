package com.studentjobs.app.feature.auth.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ForgotPasswordViewModel : ViewModel() {

    private val auth =
        FirebaseAuth.getInstance()

    private val _state =
        MutableStateFlow<UiState>(
            UiState.Idle
        )

    val state: StateFlow<UiState> =
        _state

    fun sendResetEmail(
        email: String
    ) {

        viewModelScope.launch {

            try {

                _state.value =
                    UiState.Loading

                auth.sendPasswordResetEmail(
                    email
                ).await()

                _state.value =
                    UiState.Success(
                        "Email reset đã được gửi"
                    )

            } catch (e: Exception) {

                _state.value =
                    UiState.Error(
                        e.message
                            ?: "Không thể gửi email"
                    )
            }
        }
    }
}
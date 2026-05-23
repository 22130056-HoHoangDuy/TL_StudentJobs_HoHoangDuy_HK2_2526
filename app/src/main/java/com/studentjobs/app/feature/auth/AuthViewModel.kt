package com.studentjobs.app.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.repository.auth.AuthRepository
import com.studentjobs.app.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // =========================
    // LOGIN STATE
    // =========================
    private val _loginState =
        MutableStateFlow<UiState>(UiState.Idle)

    val loginState: StateFlow<UiState> =
        _loginState

    fun login(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _loginState.value =
                UiState.Loading

            val result =
                repository.login(
                    email,
                    password
                )

            _loginState.value =
                result.fold(

                    onSuccess = { userCore ->

                        UiState.Success<UserCore>(
                            userCore
                        )
                    },

                    onFailure = { e ->

                        UiState.Error(
                            e.message
                                ?: "Login failed"
                        )
                    }
                )
        }
    }

    // =========================
    // REGISTER STATE
    // =========================
    private val _registerState =
        MutableStateFlow<UiState>(UiState.Idle)

    val registerState: StateFlow<UiState> =
        _registerState

    fun register(

        email: String,

        password: String,

        role: UserRole

    ) {

        viewModelScope.launch {

            _registerState.value =
                UiState.Loading

            val result =
                repository.register(
                    email,
                    password,
                    role
                )

            _registerState.value =
                result.fold(

                    onSuccess = { userCore ->

                        UiState.Success<UserCore>(
                            userCore
                        )
                    },

                    onFailure = { e ->

                        UiState.Error(
                            e.message
                                ?: "Register failed"
                        )
                    }
                )
        }
    }
}
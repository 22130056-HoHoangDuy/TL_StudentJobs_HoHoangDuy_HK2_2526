package com.studentjobs.app.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.repository.auth.AuthRepository
import com.studentjobs.app.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(

    private val authRepository: AuthRepository,

    private val userRepository: UserRepository

) : ViewModel() {

    private val _uiState =
        MutableStateFlow<SplashUiState>(
            SplashUiState.Loading
        )

    val uiState: StateFlow<SplashUiState> =
        _uiState.asStateFlow()

    init {
        checkSession()
    }

    fun checkSession() {

        viewModelScope.launch {

            try {

                // ==========================
                // FIREBASE SESSION
                // ==========================

                val firebaseUser =
                    authRepository.getCurrentUser()

                if (firebaseUser == null) {

                    _uiState.value =
                        SplashUiState.NavigateLogin

                    return@launch
                }

                // ==========================
                // LOAD USER CORE
                // ==========================

                val userCore =

                    userRepository.getUserCore(
                        firebaseUser.uid
                    )

                if (userCore == null) {

                    authRepository.logout()

                    _uiState.value =
                        SplashUiState.NavigateLogin

                    return@launch
                }

                // ==========================
                // SUCCESS
                // ==========================

                _uiState.value =

                    SplashUiState.NavigateMain(

                        showVerificationDialog =
                            !userCore.userVerified

                    )

            } catch (e: Exception) {

                e.printStackTrace()

                authRepository.logout()

                _uiState.value =
                    SplashUiState.NavigateLogin
            }
        }
    }
}
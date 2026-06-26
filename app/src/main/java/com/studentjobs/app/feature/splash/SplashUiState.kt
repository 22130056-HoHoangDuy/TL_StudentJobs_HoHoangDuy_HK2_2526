package com.studentjobs.app.feature.splash

sealed interface SplashUiState {

    data object Loading : SplashUiState

    data object NavigateLogin : SplashUiState

    data object NavigateVerificationGate : SplashUiState

    data object NavigateMain : SplashUiState
}
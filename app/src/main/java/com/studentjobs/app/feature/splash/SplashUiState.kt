package com.studentjobs.app.feature.splash

sealed interface SplashUiState {

    data object Loading : SplashUiState

    data object NavigateLogin : SplashUiState

    data class NavigateMain(

        val showVerificationDialog: Boolean

    ) : SplashUiState
}
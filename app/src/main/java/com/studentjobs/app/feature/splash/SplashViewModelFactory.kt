package com.studentjobs.app.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.studentjobs.app.data.repository.auth.AuthRepository
import com.studentjobs.app.data.repository.user.UserRepository

class SplashViewModelFactory(

    private val authRepository: AuthRepository,

    private val userRepository: UserRepository

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {

            return SplashViewModel(

                authRepository = authRepository,

                userRepository = userRepository

            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
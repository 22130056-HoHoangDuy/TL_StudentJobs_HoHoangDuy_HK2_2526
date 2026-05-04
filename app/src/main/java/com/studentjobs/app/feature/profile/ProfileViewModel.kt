package com.studentjobs.app.feature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.firebase.firestore.UserService
import com.studentjobs.app.utils.AppPreferences
import com.studentjobs.app.utils.calculateTrustScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userService = UserService()
    private val prefs = AppPreferences(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadUser()
    }

    private fun loadUser() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            _uiState.value = _uiState.value.copy(isLoading = false)
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        userService.listenUser(uid) { user ->

            if (user != null) {

                val score = calculateTrustScore(user)

                // 🔥 giữ nguyên logic cũ
                prefs.saveUserRole(user.role.name)

                _uiState.value = _uiState.value.copy(
                    name = user.name,
                    email = user.email,
                    role = user.role,
                    trustScore = score,
                    isStudentVerified = user.isStudentVerified,
                    isPhoneVerified = user.isPhoneVerified,
                    isEmailVerified = user.isEmailVerified,
                    isLoading = false
                )

            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
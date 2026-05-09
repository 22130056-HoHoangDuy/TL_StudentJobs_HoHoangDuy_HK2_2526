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

        val uid = FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid

        if (uid == null) {

            _uiState.value = _uiState.value.copy(
                isLoading = false
            )

            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true
        )

        userService.listenUser(uid) { user ->

            val score = calculateTrustScore(user)

            // Save role locally
            prefs.saveUserRole(user.role.name)

            _uiState.value = _uiState.value.copy(

                // ===== SYSTEM =====
                isLoading = false,

                // ===== ROLE =====
                role = user.role,

                // ===== BASIC =====
                name = user.name,
                email = user.email,
                avatarUrl = user.avatarUrl ?: "",

                // ===== VERIFICATION =====
                isStudentVerified = user.isStudentVerified,
                isPhoneVerified = user.isPhoneVerified,
                isEmailVerified = user.isEmailVerified,
                isStudentEmailVerified = user.isStudentEmailVerified,
                isBusinessVerified = user.isBusinessVerified,

                // ===== OCR =====
                extractedName = user.extractedName ?: "",
                studentId = user.studentId ?: "",
                school = user.school ?: "",
                dateOfBirth = user.dateOfBirth ?: "",

                // ===== CONTACT =====
                phone = user.phoneNumber ?: "",
                studentEmail = user.studentEmail ?: "",

                // ===== PROFILE =====
                bio = user.bio ?: "",
                major = user.major ?: "",

                // ===== SKILLS =====
                skills = user.skills,

                // ===== TRUST =====
                trustScore = score
            )
        }
    }
}
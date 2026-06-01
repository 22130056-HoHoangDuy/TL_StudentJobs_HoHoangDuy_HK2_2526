package com.studentjobs.app.feature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.repository.profile.ProfileRepository
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.StudentService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import com.studentjobs.app.firebase.firestore.VerificationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    // ========================================
    // SERVICES
    // ========================================
    private val repository = ProfileRepository(

        userService = UserServiceNew(),

        studentService = StudentService(),

        employerService = EmployerService(),

        verificationService = VerificationService()
    )

    // ========================================
    // UI STATE
    // ========================================
    private val _uiState =
        MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> =
        _uiState

    // ========================================
    // INIT
    // ========================================
    init {
        loadProfile()
    }

    // ========================================
    // LOAD PROFILE
    // ========================================
    fun loadProfile() {

        val uid = FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid

        if (uid == null) {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = false
                )

            return
        }

        viewModelScope.launch {

            try {

                // ========================================
                // USER CORE
                // ========================================
                val userCore =
                    repository.getUserCore(uid)

                if (userCore == null) {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false
                        )

                    return@launch
                }

                // ========================================
                // STUDENT FLOW
                // ========================================
                if (userCore.role == UserRole.STUDENT) {

                    val studentProfile =
                        repository.getStudentProfile(uid)

                    val studentVerification =
                        repository.getStudentVerification(uid)

                    // INITIAL STATE
                    _uiState.value =
                        _uiState.value.copy(

                            // SYSTEM
                            isLoading = false,

                            // USER
                            userCore = userCore,

                            // ROLE
                            role = userCore.role,

                            // STUDENT
                            studentProfile = studentProfile,

                            studentVerification =
                                studentVerification
                        )

                    // REALTIME LISTENER
                    repository.listenStudentVerification(uid) {

                            updatedVerification ->

                        _uiState.value =
                            _uiState.value.copy(

                                studentVerification =
                                    updatedVerification
                            )
                    }
                }
                // ========================================
                // EMPLOYER FLOW
                // ========================================
                else {

                    val employerProfile =
                        repository.getEmployerProfile(uid)

                    val employerVerification =
                        repository.getEmployerVerification(uid)

                    _uiState.value =
                        _uiState.value.copy(

                            // SYSTEM
                            isLoading = false,

                            // USER
                            userCore = userCore,

                            // ROLE
                            role = userCore.role,

                            // EMPLOYER
                            employerProfile =
                                employerProfile,

                            employerVerification =
                                employerVerification
                        )
                }

            } catch (e: Exception) {

                e.printStackTrace()

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false
                    )
            }
        }
        repository.listenUserCore(uid) {

                userCore ->

            _uiState.value =
                _uiState.value.copy(
                    userCore = userCore
                )
        }
    }
    fun updateStudentLocation(
        latitude: Double,
        longitude: Double
    ) {

        val profile =
            _uiState.value.studentProfile
                ?: return

        viewModelScope.launch {

            val updatedProfile =

                profile.copy(

                    studentLatitude = latitude,

                    studentLongitude = longitude,

                    studentLocationUrl =
                        "https://maps.google.com/?q=$latitude,$longitude",

                    updatedAt =
                        System.currentTimeMillis()
                )

            repository
                .updateStudentProfile(
                    updatedProfile
                )

            loadProfile()
        }
    }
}
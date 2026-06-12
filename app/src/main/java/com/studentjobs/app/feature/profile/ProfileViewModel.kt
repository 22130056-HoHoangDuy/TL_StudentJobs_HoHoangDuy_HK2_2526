package com.studentjobs.app.feature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.profile.ProfileRepository
import com.studentjobs.app.firebase.firestore.EmployerService
import com.studentjobs.app.firebase.firestore.JobService
import com.studentjobs.app.firebase.firestore.ShiftService
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


    private val jobRepository =
        JobRepository(
            JobService(),
            ShiftService(),
            EmployerService()
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

                    val jobs =
                        jobRepository.getJobsByEmployer(uid)

                    val totalJobs =
                        jobs.size

                    val activeJobs =
                        jobs.count {
                            it.status == "ACTIVE"
                        }

                    val ongoingJobs =
                        jobs.count {
                            it.status == "ON_GOING"
                        }

                    val completedJobs =
                        jobs.count {
                            it.status == "COMPLETED"
                        }

                    val employerVerification =
                        repository.getEmployerVerification(uid)

                    _uiState.value =
                        _uiState.value.copy(

                            isLoading = false,

                            userCore = userCore,

                            role = userCore.role,

                            employerProfile = employerProfile,

                            employerVerification =
                                employerVerification,

                            totalJobs =
                                totalJobs,

                            activeJobs =
                                activeJobs,

                            ongoingJobs =
                                ongoingJobs,

                            completedJobs =
                                completedJobs
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

    // student pick location
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

    // student selected skills
    fun updateStudentSkills(
        categories: List<String>,
        skills: List<String>
    ) {

        val profile =
            _uiState.value.studentProfile
                ?: return

        viewModelScope.launch {

            val updatedProfile =

                profile.copy(

                    preferredJobCategories =
                        categories,

                    skills = skills,

                    updatedAt =
                        System.currentTimeMillis()
                )

            repository.updateStudentProfile(
                updatedProfile
            )

            loadProfile()
        }
    }
}
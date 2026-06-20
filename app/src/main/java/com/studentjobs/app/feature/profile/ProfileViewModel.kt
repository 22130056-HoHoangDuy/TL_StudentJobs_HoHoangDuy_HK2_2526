package com.studentjobs.app.feature.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.employer.EmployerProfile
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ProfileRepository(
        UserServiceNew(),
        StudentService(),
        EmployerService(),
        VerificationService()
    )

    private val jobRepository = JobRepository(
        JobService(),
        ShiftService(),
        EmployerService()
    )

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> =
        _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser
                ?.uid ?: return

        _uiState.update {
            it.copy(isLoading = true)
        }

        repository.listenUserCore(uid) { userCore ->

            _uiState.update {

                it.copy(
                    userCore = userCore,
                    role = userCore?.role ?: UserRole.STUDENT,
                    isLoading = false
                )
            }

            if (userCore != null) {

                setupRoleSpecificListeners(
                    uid,
                    userCore.role
                )
            }
        }
    }

    private fun setupRoleSpecificListeners(
        uid: String,
        role: UserRole
    ) {

        if (role == UserRole.STUDENT) {

            repository.listenStudentProfile(uid) { profile ->

                android.util.Log.e(
                    "PROFILE_DEBUG",
                    "STUDENT_PROFILE = $profile"
                )

                _uiState.update {
                    it.copy(
                        studentProfile = profile
                    )
                }
            }

            repository.listenStudentVerification(uid) { verification ->

                android.util.Log.e(
                    "PROFILE_DEBUG",
                    "STUDENT_VERIFICATION = $verification"
                )

                _uiState.update {
                    it.copy(
                        studentVerification = verification
                    )
                }
            }
        }

        if (role == UserRole.EMPLOYER) {

            repository.listenEmployerProfile(uid) { profile ->

                _uiState.update {
                    it.copy(
                        employerProfile = profile
                    )
                }
            }

            repository.listenEmployerVerification(uid) { verification ->

                _uiState.update {
                    it.copy(
                        employerVerification = verification
                    )
                }
            }

            observeRecruitmentStats(uid)
        }
    }

    private fun observeRecruitmentStats(
        employerUid: String
    ) {

        jobRepository.listenJobsByEmployer(
            employerUid
        ) { jobs ->

            _uiState.update {

                it.copy(

                    totalJobs =
                        jobs.size,

                    activeJobs =
                        jobs.count { job ->
                            job.status == "ACTIVE"
                        },

                    ongoingJobs =
                        jobs.count { job ->
                            job.status == "ONGOING"
                        },

                    completedJobs =
                        jobs.count { job ->
                            job.status == "COMPLETED"
                        }
                )
            }
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
                    updatedAt = Date()
                )

            repository.updateStudentProfile(
                updatedProfile
            )
        }
    }

    fun updateLocalAvatar(
        uri: String
    ) {

        _uiState.update { currentState ->

            val currentProfile =
                currentState.employerProfile
                    ?: EmployerProfile()

            currentState.copy(
                employerProfile =
                    currentProfile.copy(
                        tempAvatarUri = uri
                    )
            )
        }
    }

    fun updateEmployerAvatar(
        uri: Uri
    ) {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser
                ?.uid ?: return

        viewModelScope.launch {

            val downloadUrl =
                repository.uploadEmployerStorageFile(
                    "business_logos/$uid.jpg",
                    uri
                )

            val currentProfile =
                _uiState.value.employerProfile
                    ?: return@launch

            repository.updateEmployerProfile(
                uid,
                currentProfile.copy(
                    businessLogoUrl = downloadUrl
                )
            )
        }
    }

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

                    skills =
                        skills,

                    updatedAt =
                        Date()
                )

            repository.updateStudentProfile(
                updatedProfile
            )
                .onSuccess {

                    android.util.Log.e(
                        "PROFILE_TEST",
                        "SAVE SKILLS SUCCESS"
                    )
                }
                .onFailure {

                    android.util.Log.e(
                        "PROFILE_TEST",
                        "SAVE SKILLS FAIL",
                        it
                    )
                }
        }
    }
}
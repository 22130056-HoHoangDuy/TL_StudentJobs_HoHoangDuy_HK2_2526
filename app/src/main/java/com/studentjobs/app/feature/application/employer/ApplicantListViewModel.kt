package com.studentjobs.app.feature.application.employer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.model.application.ApplicantItem
import com.studentjobs.app.data.model.application.ApplicationEntity
import com.studentjobs.app.data.repository.application.ApplicationRepository
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.student.StudentRepository
import com.studentjobs.app.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ApplicantListViewModel(

    private val applicationRepository:
    ApplicationRepository,

    private val studentRepository:
    StudentRepository,

    private val userRepository:
    UserRepository,

    private val jobRepository:
    JobRepository,

    private val jobId: String,
) : ViewModel() {

    private val _uiState =

        MutableStateFlow(
            ApplicantListUiState()
        )

    val uiState:
            StateFlow<ApplicantListUiState> =
        _uiState.asStateFlow()

    init {

        loadApplications()
    }

    fun loadApplications() {

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = true
                    )

                val applications =

                    applicationRepository
                        .getApplicationsByJob(jobId)

                        .filter {

                            it.status == "PENDING"
                        }

                val applicants =

                    applications.map { application ->

                        val studentProfile =

                            studentRepository
                                .getStudentProfile(
                                    application.studentUid
                                )

                        val userCore =

                            userRepository
                                .getUserCore(
                                    application.studentUid
                                )

                        ApplicantItem(

                            application = application,

                            studentProfile = studentProfile,

                            userCore = userCore
                        )
                    }

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = false,

                        applicants = applicants
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = false,

                        errorMessage =
                            e.message
                    )
            }
        }
    }

    fun rejectApplicant(
        applicationId: String
    ) {

        viewModelScope.launch {

            try {

                applicationRepository
                    .updateStatus(
                        applicationId,
                        "REJECTED"
                    )
                    .getOrThrow()

                loadApplications()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun acceptApplicant(
        application: ApplicationEntity
    ) {

        viewModelScope.launch {

            try {

                // Kiểm tra job trước
                val currentJob =

                    jobRepository
                        .getJob(
                            application.jobId
                        )

                        ?: return@launch

                if (
                    currentJob.acceptedApplicants >=
                    currentJob.requiredApplicants
                ) {

                    return@launch
                }

                // 1. ACCEPT APPLICATION
                applicationRepository
                    .updateStatus(

                        application.applicationId,

                        "ACCEPTED"

                    )
                    .getOrThrow()

                // 2. acceptedApplicants++
                jobRepository
                    .incrementAcceptedApplicantCount(
                        application.jobId
                    )
                    .getOrThrow()

                // 3. đọc lại job mới nhất
                val updatedJob =

                    jobRepository
                        .getJob(
                            application.jobId
                        )

                        ?: return@launch

                // 4. đủ người chưa?
                if (

                    updatedJob.acceptedApplicants >=
                    updatedJob.requiredApplicants

                ) {

                    jobRepository
                        .updateJob(

                            updatedJob.copy(
                                status = "ON_GOING"
                            )

                        )
                        .getOrThrow()
                }

                loadApplications()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}
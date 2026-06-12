package com.studentjobs.app.feature.application.employer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.model.application.ApplicantItem
import com.studentjobs.app.data.model.application.ApplicationEntity
import com.studentjobs.app.data.model.application.ApplicationStatus
import com.studentjobs.app.data.repository.application.ApplicationRepository
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.student.StudentRepository
import com.studentjobs.app.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActiveJobViewModel(

    private val applicationRepository:
    ApplicationRepository,

    private val studentRepository:
    StudentRepository,

    private val userRepository:
    UserRepository,

    private val jobRepository:
    JobRepository,

    private val jobId: String

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ActiveJobUiState()
        )

    val uiState:
            StateFlow<ActiveJobUiState> =
        _uiState.asStateFlow()

    init {

        loadApplicants()
    }

    fun loadApplicants() {

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = true
                    )

                val applications =

                    applicationRepository
                        .getApplicationsByJob(
                            jobId
                        )
                        .filter {

                            it.status ==
                                    ApplicationStatus.ACCEPTED.name

                                    ||

                                    it.status ==
                                    ApplicationStatus.WORKING.name
                        }

                val applicants =

                    applications.map { application ->

                        ApplicantItem(

                            application = application,

                            studentProfile =
                                studentRepository
                                    .getStudentProfile(
                                        application.studentUid
                                    ),

                            userCore =
                                userRepository
                                    .getUserCore(
                                        application.studentUid
                                    )
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

                        errorMessage = e.message
                    )
            }
        }
    }

    fun completeWork(
        application: ApplicationEntity
    ) {

        viewModelScope.launch {

            try {

                applicationRepository
                    .updateStatus(

                        application.applicationId,

                        ApplicationStatus
                            .COMPLETED
                            .name
                    )

                checkJobCompleted()

                loadApplicants()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private suspend fun checkJobCompleted() {

        val applications =

            applicationRepository
                .getApplicationsByJob(
                    jobId
                )

        val acceptedWorkers =

            applications.filter {

                it.status ==
                        ApplicationStatus.ACCEPTED.name

                        ||

                        it.status ==
                        ApplicationStatus.WORKING.name

                        ||

                        it.status ==
                        ApplicationStatus.COMPLETED.name
            }

        val completedWorkers =

            applications.count {

                it.status ==
                        ApplicationStatus.COMPLETED.name
            }

        if (

            acceptedWorkers.isNotEmpty()

            &&

            completedWorkers ==
            acceptedWorkers.size

        ) {

            val job =

                jobRepository
                    .getJob(jobId)

                    ?: return

            jobRepository
                .updateJob(

                    job.copy(
                        status = "COMPLETED"
                    )
                )
        }
    }
}
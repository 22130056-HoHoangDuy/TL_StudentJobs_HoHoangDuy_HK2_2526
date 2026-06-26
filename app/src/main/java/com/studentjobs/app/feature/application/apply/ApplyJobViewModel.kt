package com.studentjobs.app.feature.application.apply

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
import java.util.Date
import java.util.UUID
import com.studentjobs.app.data.model.notification.NotificationEntity
import com.studentjobs.app.data.model.notification.NotificationType
import com.studentjobs.app.data.repository.notification.NotificationRepository


class ApplyJobViewModel(

    private val applicationRepository:
    ApplicationRepository,

    private val studentRepository:
    StudentRepository,

    private val jobRepository:
    JobRepository,

    private val userRepository:
    UserRepository,

    private val notificationRepository:
    NotificationRepository

) : ViewModel() {


    private val _uiState =

        MutableStateFlow(
            ApplyJobUiState()
        )

    val uiState:
            StateFlow<ApplyJobUiState> =
        _uiState.asStateFlow()

    fun applyJob(
        jobId: String
    ) {

        Log.d(
            "APPLY_JOB",
            "===== APPLY START ====="
        )

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = true,
                        error = null,
                        limitType = ApplyJobLimitType.NONE
                    )

                val uid =

                    FirebaseAuth
                        .getInstance()
                        .currentUser
                        ?.uid

                        ?: throw Exception(
                            "User not logged in"
                        )

                Log.d(
                    "APPLY_JOB",
                    "STEP 1 - uid = $uid"
                )

                /**
                 * Check already applied first
                 */
                val alreadyApplied =

                    applicationRepository
                        .hasApplied(
                            uid,
                            jobId
                        )

                Log.d(
                    "APPLY_JOB",
                    "STEP 2 - alreadyApplied = $alreadyApplied"
                )

                Log.d(
                    "APPLY_JOB",
                    "STEP 2 - alreadyApplied = $alreadyApplied"
                )

                if (alreadyApplied) {

                    _uiState.value =
                        _uiState.value.copy(

                            isLoading = false,

                            limitType =
                                ApplyJobLimitType
                                    .ALREADY_APPLIED
                        )

                    return@launch
                }

                /**
                 * Check PLUS
                 */
                val isPlus =

                    userRepository
                        .isPlusActive(uid)

                Log.d(
                    "PLUS_CHECK",
                    "isPlus = $isPlus"
                )

                /**
                 * Count active jobs
                 */
                val activeCount =

                    applicationRepository
                        .countActiveApplications(
                            uid
                        )

                Log.d(
                    "APPLY_JOB",
                    "STEP 3 - activeCount = $activeCount"
                )

                val maxAllowedJobs =

                    if (isPlus)
                        2
                    else
                        1

                /**
                 * Check limit
                 */
                if (
                    activeCount >= maxAllowedJobs
                ) {

                    _uiState.value =
                        _uiState.value.copy(

                            isLoading = false,

                            limitType =

                                if (isPlus)

                                    ApplyJobLimitType
                                        .PLUS_LIMIT_REACHED
                                else

                                    ApplyJobLimitType
                                        .FREE_LIMIT_REACHED
                        )

                    return@launch
                }

                /**
                 * Student profile
                 */
                val student =

                    studentRepository
                        .getStudentProfile(
                            uid
                        )

                        ?: throw Exception(
                            "Student profile not found"
                        )

                Log.d(
                    "APPLY_JOB",
                    "STEP 4 - Student found = ${student.fullName}"
                )

                /**
                 * Job
                 */
                val job =

                    jobRepository
                        .getJob(
                            jobId
                        )

                        ?: throw Exception(
                            "Job not found"
                        )

                Log.d(
                    "APPLY_JOB",
                    "STEP 5 - Job found = ${job.title}"
                )

                /**
                 * Create application
                 */
                val application =

                    ApplicationEntity(

                        applicationId =
                            UUID.randomUUID()
                                .toString(),

                        studentUid =
                            uid,

                        employerUid =
                            job.employerUid,

                        jobId =
                            job.jobId,

                        status =
                            ApplicationStatus
                                .PENDING
                                .name,

                        studentName =
                            student.fullName,

                        schoolName =
                            student.schoolName
                                ?: "",

                        jobTitle =
                            job.title,

                        businessName =
                            job.businessName,

                        appliedAt = Date()
                    )

                Log.d(
                    "APPLY_JOB",
                    "STEP 6 - Saving Firestore"
                )

                val result =

                    applicationRepository
                        .createApplication(
                            application
                        )

                result.getOrThrow()

                jobRepository
                    .incrementApplicantCount(
                        job.jobId
                    )
                notificationRepository.createNotification(

                    NotificationEntity(

                        notificationId =
                            UUID.randomUUID().toString(),

                        receiverUid = uid,

                        title = "Ứng tuyển thành công",

                        message =
                            "Bạn đã ứng tuyển vào công việc ${job.title}.",

                        type =
                            NotificationType
                                .JOB_APPLIED
                                .name,

                        relatedId =
                            job.jobId,

                        createdAt = Date()
                    )
                )

                Log.d(
                    "APPLY_JOB",
                    "STEP 7 - SUCCESS"
                )

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = false,

                        isSuccess = true,

                        hasApplied = true,

                        error = null
                    )

            } catch (e: Exception) {

                Log.e(
                    "APPLY_JOB",
                    "ERROR = ${e.message}",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = false,

                        error = e.message
                    )
            }
        }
    }

    fun checkApplied(
        jobId: String
    ) {

        viewModelScope.launch {

            val uid =
                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.uid
                    ?: return@launch

            val applied =

                applicationRepository
                    .hasApplied(
                        uid,
                        jobId
                    )

            _uiState.value =
                _uiState.value.copy(
                    hasApplied = applied
                )
        }
    }

    fun clearLimitType() {

        _uiState.value =
            _uiState.value.copy(

                limitType =
                    ApplyJobLimitType.NONE
            )
    }

}
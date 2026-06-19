package com.studentjobs.app.feature.application.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.application.ApplicationStatus
import com.studentjobs.app.data.repository.application.ApplicationRepository
import com.studentjobs.app.firebase.firestore.ApplicationService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyApplicationsViewModel : ViewModel() {

    private val repository =
        ApplicationRepository(
            ApplicationService()
        )

    private val userService =
        UserServiceNew()

    private val auth =
        FirebaseAuth.getInstance()

    private val _applyingJobs =
        MutableStateFlow<List<StudentApplicationItem>>(
            emptyList()
        )

    val applyingJobs =
        _applyingJobs.asStateFlow()

    private val _workingJobs =
        MutableStateFlow<List<StudentApplicationItem>>(
            emptyList()
        )

    val workingJobs =
        _workingJobs.asStateFlow()

    init {
        loadApplications()
    }

    private fun loadApplications() {

        val uid =
            auth.currentUser?.uid
                ?: return

        viewModelScope.launch {

            val applications =
                repository.getApplicationsByStudent(
                    uid
                )

            val uiApplications =

                applications.map { app ->

                    val employerUser =

                        userService.getUserCore(
                            app.employerUid
                        )
// DEBUG: In ra để xem status trong Firestore thực tế đang lưu là gì
                    println("DEBUG_STATUS: Job: ${app.jobTitle}, Status in DB: '${app.status}'")
                    StudentApplicationItem(

                        application = app,

                        employerPhone =
                            employerUser?.phoneNumber
                    )
                }

            _applyingJobs.value =

                uiApplications.filter {

                    it.application.status ==
                            ApplicationStatus.PENDING.name
                }

            _workingJobs.value =

                uiApplications.filter {

                    it.application.status ==
                            ApplicationStatus.ACCEPTED.name
                            ||
                            it.application.status ==
                            ApplicationStatus.WORKING.name
                }
        }
    }
}
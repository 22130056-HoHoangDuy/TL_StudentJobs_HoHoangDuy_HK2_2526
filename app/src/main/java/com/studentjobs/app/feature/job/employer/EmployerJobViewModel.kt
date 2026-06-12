package com.studentjobs.app.feature.job.employer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployerJobViewModel(

    private val repository: JobRepository

) : ViewModel() {

    private val auth =
        FirebaseAuth.getInstance()

    private val userService =
        UserServiceNew()

    private val _uiState =
        MutableStateFlow(
            EmployerJobUiState()
        )

    val uiState:
            StateFlow<EmployerJobUiState> =
        _uiState.asStateFlow()

    init {

        loadData()
    }

    fun loadData() {
        Log.d(
            "EMPLOYER_JOB",
            "LOAD DATA"
        )

        val uid =
            auth.currentUser?.uid
                ?: return

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = true
                    )

                val jobs =
                    repository
                        .getJobsByEmployer(uid)

                val user =
                    userService
                        .getUserCore(uid)

                val runningJobCount =

                    jobs.count {

                        it.status == "ACTIVE"

                                ||

                                it.status == "ON_GOING"
                    }

                val maxAllowed =

                    when (
                        user?.subscriptionPlan
                    ) {

                        SubscriptionPlan.PLUS ->
                            5

                        else ->
                            2
                    }

                _uiState.value =

                    _uiState.value.copy(

                        jobs = jobs,

                        activeJobCount =
                            runningJobCount,

                        maxJobAllowed =
                            maxAllowed,

                        isLoading = false
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
}
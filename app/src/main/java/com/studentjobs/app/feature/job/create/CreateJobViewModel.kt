package com.studentjobs.app.feature.job.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.model.job.JobEntity
import com.studentjobs.app.data.model.job.ShiftEntity
import com.studentjobs.app.data.repository.job.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CreateJobViewModel(

    private val repository:
    JobRepository

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            CreateJobUiState()
        )

    val uiState:
            StateFlow<CreateJobUiState> =
        _uiState.asStateFlow()

    //
    private val draftJobId =
        UUID.randomUUID().toString()

    //
    init {

        _uiState.value =
            _uiState.value.copy(

                draftJobId = draftJobId
            )
    }

    // ========================================
    // BASIC
    // ========================================

    fun updateTitle(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                title = value
            )
    }

    fun updateDescription(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                description = value
            )
    }

    fun updateSalaryMin(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                salaryMin = value
            )
    }

    fun updateSalaryMax(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                salaryMax = value
            )
    }

    fun updateRequiredApplicants(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                requiredApplicants = value
            )
    }

    // ========================================
    // SKILL
    // ========================================

    fun toggleSkill(
        skill: String
    ) {

        val current =

            _uiState.value
                .selectedSkills
                .toMutableList()

        if (
            current.contains(skill)
        ) {

            current.remove(skill)

        } else {

            current.add(skill)
        }

        _uiState.value =
            _uiState.value.copy(
                selectedSkills = current
            )
    }

    // ========================================
    // SHIFT
    // ========================================

    fun addShift(
        shift: ShiftEntity
    ) {

        _uiState.value =
            _uiState.value.copy(

                shifts =
                    _uiState.value.shifts +
                            shift
            )
    }

    fun removeShift(
        shiftId: String
    ) {

        _uiState.value =
            _uiState.value.copy(

                shifts =

                    _uiState.value.shifts
                        .filter {

                            it.shiftId != shiftId
                        }
            )
    }

    // ========================================
    // CREATE JOB
    // ========================================

    fun createJob(

        employerUid: String

    ) {

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = true
                    )

                val job =

                    JobEntity(

                        jobId =
                            UUID.randomUUID()
                                .toString(),

                        employerUid =
                            employerUid,

                        title =
                            _uiState.value.title,

                        description =
                            _uiState.value.description,

                        salaryMin =
                            _uiState.value
                                .salaryMin
                                .toDoubleOrNull()
                                ?: 0.0,

                        salaryMax =
                            _uiState.value
                                .salaryMax
                                .toDoubleOrNull()
                                ?: 0.0,

                        requiredSkills =
                            _uiState.value
                                .selectedSkills,

                        requiredApplicants =
                            _uiState.value
                                .requiredApplicants
                                .toIntOrNull()
                                ?: 1,

                        autoRecruitmentEnabled =
                            _uiState.value
                                .autoRecruitmentEnabled,

                        createdAt =
                            System.currentTimeMillis(),

                        updatedAt =
                            System.currentTimeMillis()
                    )

                repository.createJob(

                    job,

                    _uiState.value.shifts

                )

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = false,

                        success = true
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

    private fun convertTimeToMinute(
        time: String
    ): Int {

        val parts =
            time.split(":")

        val hour =
            parts[0].toInt()

        val minute =
            parts[1].toInt()

        return hour * 60 + minute
    }
}
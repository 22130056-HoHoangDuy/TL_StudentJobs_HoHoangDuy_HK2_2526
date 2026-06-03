package com.studentjobs.app.feature.job.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.job.JobEntity
import com.studentjobs.app.data.model.job.ShiftEntity
import com.studentjobs.app.data.model.skill.SkillCatalog
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CreateJobViewModel(

    private val repository: JobRepository

) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateJobUiState()
    )

    //
    fun toggleAutoRecruitment() {

        if (
            !_uiState.value.isPlusEmployer
        ) {
            return
        }

        _uiState.value =

            _uiState.value.copy(

                autoRecruitmentEnabled =

                    !_uiState.value
                        .autoRecruitmentEnabled
            )
    }

    private val auth =
        FirebaseAuth.getInstance()

    private val userService =
        UserServiceNew()

    val uiState: StateFlow<CreateJobUiState> = _uiState.asStateFlow()

    //
    private val draftJobId = UUID.randomUUID().toString()

    //
    init {

        _uiState.value =
            _uiState.value.copy(
                draftJobId = draftJobId
            )

        loadAvailableSkills()

        loadSubscriptionPlan()
    }

    private fun loadAvailableSkills() {

        val uid =
            auth.currentUser?.uid
                ?: return

        viewModelScope.launch {

            try {

                val category =

                    repository
                        .getEmployerCategory(uid)

                val skills =

                    SkillCatalog
                        .getSkillsByCategory(
                            category
                        )

                _uiState.value =
                    _uiState.value.copy(

                        availableSkills =

                            skills.map {
                                it.skillName
                            }
                    )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private fun loadSubscriptionPlan() {

        val uid =
            auth.currentUser?.uid
                ?: return

        viewModelScope.launch {

            try {

                val user =

                    userService
                        .getUserCore(uid)

                _uiState.value =

                    _uiState.value.copy(

                        isPlusEmployer =

                            user?.subscriptionPlan ==

                                    SubscriptionPlan.PLUS
                    )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }


    // ========================================
    // BASIC
    // ========================================

    fun updateTitle(
        value: String
    ) {

        _uiState.value = _uiState.value.copy(
            title = value
        )
    }

    fun updateDescription(
        value: String
    ) {

        _uiState.value = _uiState.value.copy(
            description = value
        )
    }

    fun updateSalaryMin(
        value: String
    ) {

        _uiState.value = _uiState.value.copy(
            salaryMin = value
        )
    }

    fun updateSalaryMax(
        value: String
    ) {

        _uiState.value = _uiState.value.copy(
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

            _uiState.value.selectedSkills.toMutableList()

        if (current.contains(skill)) {

            current.remove(skill)

        } else {

            current.add(skill)
        }

        _uiState.value = _uiState.value.copy(
            selectedSkills = current
        )
    }

    // ========================================
    // SHIFT
    // ========================================

    fun addShift() {

        val shift = ShiftEntity(

            shiftId =
                UUID.randomUUID().toString(),

            jobId =
                _uiState.value.draftJobId,

            dayOfWeek =
                _uiState.value.selectedDay,

            startMinute =
                convertTimeToMinute(
                    _uiState.value.startMinute
                ),

            endMinute =
                convertTimeToMinute(
                    _uiState.value.endMinute
                ),

            createdAt =
                System.currentTimeMillis()
        )

        _uiState.value =
            _uiState.value.copy(

                shifts =
                    _uiState.value.shifts + shift,

                startMinute = "",

                endMinute = "",
            )
    }

    fun removeShift(
        shiftId: String
    ) {

        _uiState.value = _uiState.value.copy(

            shifts =

                _uiState.value.shifts.filter {

                    it.shiftId != shiftId
                })
    }

    // ========================================
    // CREATE JOB
    // ========================================

    fun createJob(

        employerUid: String

    ) {
        val error = validate()

        if (error != null) {

            _uiState.value =
                _uiState.value.copy(
                    errorMessage = error
                )

            return
        }
        viewModelScope.launch {

            try {

                _uiState.value = _uiState.value.copy(
                    isLoading = true
                )

                val job =

                    JobEntity(

                        jobId =
                            _uiState.value.draftJobId,

                        employerUid = employerUid,

                        title = _uiState.value.title,

                        description = _uiState.value.description,

                        salaryMin = _uiState.value.salaryMin.toDoubleOrNull() ?: 0.0,

                        salaryMax = _uiState.value.salaryMax.toDoubleOrNull() ?: 0.0,

                        requiredSkills = _uiState.value.selectedSkills,

                        requiredApplicants = _uiState.value.requiredApplicants.toIntOrNull() ?: 1,

                        autoRecruitmentEnabled = _uiState.value.autoRecruitmentEnabled,

                        createdAt = System.currentTimeMillis(),

                        updatedAt = System.currentTimeMillis()
                    )

                repository.createJob(

                    job,

                    _uiState.value.shifts

                )

                _uiState.value = _uiState.value.copy(

                    isLoading = false,

                    success = true
                )

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(

                    isLoading = false,

                    errorMessage = e.message
                )
            }
        }
    }

    private fun convertTimeToMinute(
        time: String
    ): Int {

        return try {

            val parts =
                time.split(":")

            val hour =
                parts[0].toInt()

            val minute =
                parts[1].toInt()

            hour * 60 + minute

        } catch (e: Exception) {

            0
        }
    }

    fun updateSelectedDay(
        value: Int
    ) {

        _uiState.value =
            _uiState.value.copy(
                selectedDay = value
            )
    }

    fun updateStartMinute(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                startMinute = value
            )
    }

    fun updateEndMinute(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                endMinute = value
            )
    }

    // validate condition
    private fun validate(): String? {

        if (_uiState.value.title.isBlank()) {
            return "Please enter job title"
        }

        if (_uiState.value.description.isBlank()) {
            return "Please enter job description"
        }

        if (_uiState.value.salaryMin.isBlank()) {
            return "Please enter minimum salary"
        }

        if (_uiState.value.salaryMax.isBlank()) {
            return "Please enter maximum salary"
        }

        if (_uiState.value.selectedSkills.isEmpty()) {
            return "Please select at least one skill"
        }

        if (_uiState.value.shifts.isEmpty()) {
            return "Please add at least one shift"
        }

        return null
    }
}
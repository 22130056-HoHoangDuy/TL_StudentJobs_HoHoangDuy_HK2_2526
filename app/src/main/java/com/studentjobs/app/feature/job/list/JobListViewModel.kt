package com.studentjobs.app.feature.job.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.repository.auth.AuthRepository
import com.studentjobs.app.data.repository.job.JobRepository
import com.studentjobs.app.data.repository.student.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JobListViewModel(
    private val jobRepository: JobRepository,
    private val studentRepository: StudentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobListUiState())
    val uiState: StateFlow<JobListUiState> = _uiState.asStateFlow()

    init {
        loadJobs()
    }

    fun loadJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val jobs = jobRepository.getActiveJobs()
            _uiState.update { it.copy(jobs = jobs, isLoading = false) }
        }
    }

    fun updateSearchText(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    fun toggleAutoApply() {
        _uiState.update { it.copy(isAutoApplyEnabled = !it.isAutoApplyEnabled) }
    }

    fun toggleSuggestedJobs() {
        viewModelScope.launch {
            val isSuggested = !_uiState.value.isViewingSuggested
            _uiState.update { it.copy(isLoading = true, isViewingSuggested = isSuggested) }
            val jobs = if (isSuggested) {
                val uid = authRepository.getCurrentUserUid()
                val profile = uid?.let { studentRepository.getStudentProfile(it) }
                jobRepository.getSuggestedJobs(profile?.skills ?: emptyList())
            } else {
                jobRepository.getActiveJobs()
            }
            _uiState.update { it.copy(jobs = jobs, isLoading = false) }
        }
    }

    // Trong JobListViewModel
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    init {
        loadCategories() // Gọi khi khởi tạo
        loadJobs()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = jobRepository.getAllCategories()
        }
    }

    // Cập nhật hàm lọc để lọc theo cả Category
    fun applyFilters(
        distance: Float,
        minSalary: Double,
        selectedSkills: List<String>,
        selectedCategories: List<String>
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val allJobs = jobRepository.getActiveJobs()

            val filtered = allJobs.filter { job ->
                val matchesSalary = job.salaryMin >= minSalary
                val matchesSkills =
                    selectedSkills.isEmpty() || job.requiredSkills.any { it in selectedSkills }
                val matchesCategory =
                    selectedCategories.isEmpty() || selectedCategories.contains(job.businessCategory)

                matchesSalary && matchesSkills && matchesCategory
            }

            _uiState.update {
                it.copy(
                    jobs = filtered,
                    isLoading = false,
                    minSalary = minSalary,
                    selectedSkills = selectedSkills
                )
            }
        }
    }

    fun applyFilters(distance: Float, minSalary: Double, skills: List<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val allJobs = jobRepository.getActiveJobs()
            val filtered = allJobs.filter { job ->
                val matchesSalary = job.salaryMin >= minSalary
                val matchesSkills = skills.isEmpty() || job.requiredSkills.any { it in skills }
                matchesSalary && matchesSkills
            }
            _uiState.update {
                it.copy(
                    jobs = filtered,
                    isLoading = false,
                    minSalary = minSalary,
                    selectedSkills = skills,
                    filterDistance = distance
                )
            }
        }
    }
}
package com.studentjobs.app.feature.job.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.repository.job.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JobListViewModel(

    private val repository: JobRepository

) : ViewModel() {

    private val _uiState =

        MutableStateFlow(
            JobListUiState()
        )

    val uiState: StateFlow<JobListUiState> =
        _uiState.asStateFlow()

    init {

        loadJobs()
    }

    fun loadJobs() {

        viewModelScope.launch {

            val jobs =
                repository.getActiveJobs()

            _uiState.value =

                _uiState.value.copy(

                    jobs = jobs,

                    isLoading = false
                )
        }
    }

    fun updateSearchText(
        text: String
    ) {

        _uiState.value =

            _uiState.value.copy(

                searchText = text
            )
    }
}
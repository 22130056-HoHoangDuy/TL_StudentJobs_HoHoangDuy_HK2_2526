package com.studentjobs.app.feature.schedule

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentjobs.app.data.repository.schedule.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(

    private val repository:
    ScheduleRepository = ScheduleRepository()

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ScheduleUiState()
        )

    val uiState:
            StateFlow<ScheduleUiState> =

        _uiState.asStateFlow()

    fun selectImage(

        uri: Uri

    ) {

        _uiState.value =
            _uiState.value.copy(

                selectedImageUri = uri
            )
    }

    fun uploadTimetable(

        uid: String

    ) {

        val imageUri =
            _uiState
                .value
                .selectedImageUri

                ?: return

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = true,

                        errorMessage = null,

                        successMessage = null
                    )

                val result =
                    repository.uploadTimetable(

                        uid = uid,

                        imageUri = imageUri
                    )

                result
                    .onSuccess { imageUrl ->

                        _uiState.value =
                            _uiState.value.copy(

                                isLoading = false,

                                uploadedImageUrl =
                                    imageUrl,

                                isProcessingOcr = true,

                                successMessage =
                                    "Upload successful. OCR processing..."
                            )
                    }

                    .onFailure {

                        _uiState.value =
                            _uiState.value.copy(

                                isLoading = false,

                                errorMessage =
                                    it.message
                                        ?: "Upload failed"
                            )
                    }

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = false,

                        errorMessage =
                            e.message
                                ?: "Unknown error"
                    )
            }
        }
    }

    fun loadSchedule(

        uid: String

    ) {

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = true
                    )

                val schedule =
                    repository.getSchedule(uid)

                _uiState.value =
                    _uiState.value.copy(

                        isLoading = false,

                        schedule = schedule,

                        isProcessingOcr = false
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

    fun clearMessage() {

        _uiState.value =
            _uiState.value.copy(

                successMessage = null,

                errorMessage = null
            )
    }
    fun observeSchedule(
        uid: String
    ) {

        repository.listenSchedule(
            uid
        ) { schedule ->

            _uiState.value =
                _uiState.value.copy(

                    schedule = schedule,

                    isProcessingOcr =
                        schedule?.ocrProcessed == false
                )
        }
    }
}
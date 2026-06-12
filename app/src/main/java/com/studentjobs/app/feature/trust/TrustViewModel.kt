package com.studentjobs.app.feature.trust

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.trust.TrustLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrustViewModel : ViewModel() {

    private val auth =
        FirebaseAuth.getInstance()

    private val _uiState =
        MutableStateFlow(
            TrustUiState()
        )

    val uiState:
            StateFlow<TrustUiState> =
        _uiState.asStateFlow()

    init {

        loadTrustData()
    }

    private fun loadTrustData() {

        val uid =
            auth.currentUser?.uid
                ?: return

        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = true
                    )

                // TODO:
                // Load trustScore từ UserCore
                // Load logs từ Firestore

                val trustScore = 50

                val logs = listOf(

                    TrustLog(
                        trustLogId = "1",
                        userUid = uid,
                        actionType = "EMAIL_VERIFIED",
                        changeAmount = 10,
                        severity = "LOW",
                        description = "Xác thực email thành công",
                        createdAt = System.currentTimeMillis()
                    ),

                    TrustLog(
                        trustLogId = "2",
                        userUid = uid,
                        actionType = "PHONE_VERIFIED",
                        changeAmount = 10,
                        severity = "LOW",
                        description = "Xác thực số điện thoại",
                        createdAt = System.currentTimeMillis()
                    ),

                    TrustLog(
                        trustLogId = "3",
                        userUid = uid,
                        actionType = "JOB_COMPLETED",
                        changeAmount = 5,
                        severity = "LOW",
                        description = "Hoàn thành công việc",
                        createdAt = System.currentTimeMillis()
                    )
                )

                _uiState.value =

                    _uiState.value.copy(

                        isLoading = false,

                        trustScore = trustScore,

                        trustLevel =
                            calculateTrustLevel(
                                trustScore
                            ),

                        logs = logs
                    )

            } catch (e: Exception) {

                e.printStackTrace()

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false
                    )
            }
        }
    }

    private fun calculateTrustLevel(
        score: Int
    ): String {

        return when {

            score >= 80 ->
                "Elite User"

            score >= 60 ->
                "Trusted User"

            score >= 40 ->
                "Verified User"

            score >= 20 ->
                "Basic User"

            else ->
                "New User"
        }
    }
}
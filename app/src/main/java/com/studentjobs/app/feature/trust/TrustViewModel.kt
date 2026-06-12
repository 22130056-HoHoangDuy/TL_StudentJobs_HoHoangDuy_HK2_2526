package com.studentjobs.app.feature.trust

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.repository.trust.TrustRepository
import com.studentjobs.app.firebase.firestore.TrustService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrustViewModel : ViewModel() {

    private val auth =
        FirebaseAuth.getInstance()

    private val repository =
        TrustRepository(
            TrustService(),
            UserServiceNew()
        )

    private val _uiState =
        MutableStateFlow(
            TrustUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    init {
        loadTrustData()
    }

    fun changeFilter(
        filter: TrustFilter
    ) {

        _uiState.value =
            _uiState.value.copy(
                selectedFilter = filter
            )
    }

    private fun calculateTrustLevel(
        score: Int
    ): String {

        return when {

            score >= 80 ->
                "Độ tin cậy: Cao"

            score >= 60 ->
                "Độ tin cậy: Trung"

            score >= 40 ->
                "Độ tin cậy: Thấp"

            score >= 20 ->
                "Độ tin cậy: Nguy hiểm"

            else ->
                "Không xác định"
        }
    }

    private fun loadTrustData() {

        val uid = auth.currentUser?.uid ?: return

        Log.d(
            "TRUST_DEBUG",
            "uid = $uid"
        )
        viewModelScope.launch {

            try {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = true
                    )

                val trustScore =
                    repository.getTrustScore(uid)

                Log.d(
                    "TRUST_DEBUG",
                    "trustScore = $trustScore"
                )

                val logs =
                    repository.getTrustLogs(uid)

                Log.d(
                    "TRUST_DEBUG",
                    "logs size = ${logs.size}"
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

                Log.d(
                    "TRUST_DEBUG",
                    "uiState updated"
                )

            } catch (e: Exception) {

                Log.e(
                    "TRUST_DEBUG",
                    "load error",
                    e
                )

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false
                    )
            }
        }
    }
}
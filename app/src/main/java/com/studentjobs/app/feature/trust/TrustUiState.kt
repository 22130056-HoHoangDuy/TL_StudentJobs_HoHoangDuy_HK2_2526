package com.studentjobs.app.feature.trust

import com.studentjobs.app.data.model.trust.TrustLog

data class TrustUiState(

    val isLoading: Boolean = false,

    val trustScore: Int = 0,

    val trustLevel: String = "",

    val logs: List<TrustLog> = emptyList(),

    val selectedFilter: TrustFilter =
        TrustFilter.ALL
)

enum class TrustFilter {

    ALL,

    POSITIVE,

    NEGATIVE
}
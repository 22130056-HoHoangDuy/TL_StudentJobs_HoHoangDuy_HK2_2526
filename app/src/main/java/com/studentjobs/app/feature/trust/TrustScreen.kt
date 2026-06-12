package com.studentjobs.app.feature.trust

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentjobs.app.feature.trust.components.TrustFilterDropdown
import com.studentjobs.app.feature.trust.components.TrustLogItem
import com.studentjobs.app.feature.trust.components.TrustScoreGauge

@Composable
fun TrustScreen(

    viewModel: TrustViewModel =
        viewModel()

) {

    val state by
    viewModel.uiState.collectAsState()

    val filteredLogs =

        when (
            state.selectedFilter
        ) {

            TrustFilter.ALL ->

                state.logs

            TrustFilter.POSITIVE ->

                state.logs.filter {

                    it.changeAmount > 0
                }

            TrustFilter.NEGATIVE ->

                state.logs.filter {

                    it.changeAmount < 0
                }
        }

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)

    ) {

        // ========================================
        // TRUST SCORE
        // ========================================

        item {

            TrustScoreGauge(

                score =
                    state.trustScore,

                level =
                    state.trustLevel
            )
        }

        // ========================================
        // FILTER
        // ========================================

        item {

            TrustFilterDropdown(

                selectedFilter =
                    state.selectedFilter,

                onFilterChange =
                    viewModel::changeFilter
            )
        }

        // ========================================
        // HISTORY
        // ========================================

        item {

            Card(

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .surfaceVariant
                    )

            ) {

                Column(

                    modifier =
                        Modifier.padding(16.dp)

                ) {

                    Text(

                        text =
                            "Lịch sử điểm uy tín",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )
                }
            }
        }

        items(filteredLogs) { log ->

            TrustLogItem(
                log = log
            )
        }
    }
}
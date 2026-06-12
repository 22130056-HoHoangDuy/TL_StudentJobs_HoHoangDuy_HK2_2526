package com.studentjobs.app.feature.trust

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentjobs.app.feature.trust.components.TrustFilterDropdown
import com.studentjobs.app.feature.trust.components.TrustGaugeCard
import com.studentjobs.app.feature.trust.components.TrustHistoryCard

@Composable
fun TrustScreen(
    viewModel: TrustViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val filteredLogs = when (state.selectedFilter) {
        TrustFilter.ALL -> state.logs
        TrustFilter.POSITIVE -> state.logs.filter { it.changeAmount > 0 }
        TrustFilter.NEGATIVE -> state.logs.filter { it.changeAmount < 0 }
    }

    // Tạo nền màu hồng/tím dịu mắt phía trên đỉnh màn hình kéo nhạt dần xuống dưới giống hệt App MoMo
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFCE7F3), Color(0xFFF8FAFC), Color(0xFFF8FAFC)),
        startY = 0f,
        endY = 1200f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TrustGaugeCard(
                    trustScore = state.trustScore,
                    trustLevel = state.trustLevel
                )
            }

            item {
                TrustFilterDropdown(
                    selectedFilter = state.selectedFilter,
                    onFilterChange = viewModel::changeFilter
                )
            }

            item {
                TrustHistoryCard(
                    logs = filteredLogs
                )
            }
        }
    }
}
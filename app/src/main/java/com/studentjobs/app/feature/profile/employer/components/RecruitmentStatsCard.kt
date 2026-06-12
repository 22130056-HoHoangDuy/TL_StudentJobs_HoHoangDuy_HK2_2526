package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun RecruitmentStatsCard(state: ProfileUiState) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Thống kê tuyển dụng",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatItem(Modifier.weight(1f), "Tổng", state.totalJobs.toString(), Color(0xFFE0E7FF))
            StatItem(
                Modifier.weight(1f),
                "Đang tuyển",
                state.activeJobs.toString(),
                Color(0xFFDCFCE7)
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatItem(
                Modifier.weight(1f),
                "Ongoing",
                state.ongoingJobs.toString(),
                Color(0xFFFEF9C3)
            )
            StatItem(
                Modifier.weight(1f),
                "Hoàn thành",
                state.completedJobs.toString(),
                Color(0xFFF3E8FF)
            )
        }
    }
}

@Composable
fun StatItem(modifier: Modifier, label: String, value: String, bgColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

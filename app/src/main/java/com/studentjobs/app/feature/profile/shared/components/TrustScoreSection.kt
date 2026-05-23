package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun TrustScoreSection(score: Int) {

    val progress = score / 100f

    val color = when {
        score >= 80 -> Color(0xFF4CAF50) // xanh
        score >= 50 -> Color(0xFFFFA000) // cam
        else -> Color(0xFFF44336) // đỏ
    }

    val label = when {
        score >= 80 -> "Trusted"
        score >= 50 -> "Medium trust"
        else -> "Low trust"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trust Score",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "$score",
                    style = MaterialTheme.typography.titleMedium,
                    color = color
                )
            }

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = color
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}

package com.studentjobs.app.feature.profile.student.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun SkillsCard(
    state: ProfileUiState
) {

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E1B4B)
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {

        Column(
            modifier = Modifier
                .background(gradient)
                .padding(22.dp)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = Color(0xFF8B5CF6)
                )

                Spacer(modifier = Modifier.height(0.dp))

                Text(
                    text = " Skills & Technologies",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Empty State
            if (state.studentProfile?.skills?.isEmpty() ?: true) {

                Text(
                    text = "Add your skills to build your student profile 🚀",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )

            } else {

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    state.studentProfile?.skills?.forEach { skill ->

                        SkillChip(skill)
                    }
                }
            }
        }
    }
}

@Composable
private fun SkillChip(
    skill: String
) {

    val chipGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2563EB),
            Color(0xFF7C3AED)
        )
    )

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = Color.Transparent
    ) {

        Row(
            modifier = Modifier
                .background(chipGradient)
                .padding(
                    horizontal = 18.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = skill,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
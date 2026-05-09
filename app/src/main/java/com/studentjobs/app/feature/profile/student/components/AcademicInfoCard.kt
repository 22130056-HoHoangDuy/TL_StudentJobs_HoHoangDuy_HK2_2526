package com.studentjobs.app.feature.profile.student.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
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
fun AcademicInfoCard(
    state: ProfileUiState
) {

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E293B)
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
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color(0xFF60A5FA)
                )

                Spacer(modifier = Modifier.height(0.dp))

                Text(
                    text = " Academic Information",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AcademicItem(
                label = "Full Name",
                value = state.extractedName.ifEmpty {
                    state.name
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AcademicItem(
                label = "Student ID",
                value = state.studentId
            )

            Spacer(modifier = Modifier.height(16.dp))

            AcademicItem(
                label = "University",
                value = state.school
            )

            Spacer(modifier = Modifier.height(16.dp))

            AcademicItem(
                label = "Student Email",
                value = state.studentEmail
            )

            Spacer(modifier = Modifier.height(16.dp))

            AcademicItem(
                label = "Date Of Birth",
                value = state.dateOfBirth
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Verified footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color(0xFF4ADE80)
                )

                Spacer(modifier = Modifier.height(0.dp))

                Text(
                    text = " OCR Verified Student Identity",
                    color = Color(0xFF4ADE80),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun AcademicItem(
    label: String,
    value: String
) {

    Column {

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Badge,
                contentDescription = null,
                tint = Color(0xFF8B5CF6)
            )

            Spacer(modifier = Modifier.height(0.dp))

            Text(
                text = value.ifEmpty { "Not Available" },
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
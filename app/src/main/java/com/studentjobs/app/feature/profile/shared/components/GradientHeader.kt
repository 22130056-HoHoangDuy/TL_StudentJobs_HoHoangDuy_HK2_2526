package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun GradientHeader(
    state: ProfileUiState
) {

    val userCore =
        state.userCore

    val studentProfile =
        state.studentProfile

    val studentVerification =
        state.studentVerification

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2563EB),
            Color(0xFF7C3AED),
            Color(0xFF06B6D4)
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(32.dp),

        color = Color.Transparent
    ) {

        Column(
            modifier = Modifier
                .background(gradient)
                .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            // ====================================
            // AVATAR
            // ====================================

            Box(
                contentAlignment =
                    Alignment.BottomEnd
            ) {

                Image(
                    painter =
                        rememberAsyncImagePainter(

                            model =
                                if (
                                    studentProfile?.avatarUrl
                                        .isNullOrBlank()
                                ) {
                                    "https://i.imgur.com/tGbaZCY.jpg"
                                } else {
                                    studentProfile?.avatarUrl
                                }
                        ),

                    contentDescription = null,

                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(
                            width = 4.dp,

                            color = Color.White,

                            shape = CircleShape
                        ),

                    contentScale =
                        ContentScale.Crop
                )
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            // ====================================
            // NAME
            // ====================================

            Text(
                text =
                    if (
                        studentVerification
                            ?.extractedStudentName
                            .isNullOrBlank()
                    ) {

                        studentProfile?.fullName
                            ?: "Student"

                    } else {

                        studentVerification
                            ?.extractedStudentName
                            ?: "Student"
                    },

                style =
                    MaterialTheme.typography
                        .headlineMedium,

                color = Color.White,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            // ====================================
            // VERIFIED
            // ====================================

            Row(
                horizontalArrangement =
                    Arrangement.Center,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Verified,

                    contentDescription = null,

                    tint =
                        Color(0xFF4ADE80)
                )

                Spacer(
                    modifier =
                        Modifier.size(6.dp)
                )

                Text(
                    text = "Verified Student",

                    color = Color.White,

                    style =
                        MaterialTheme.typography
                            .titleMedium
                )
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            // ====================================
            // SCHOOL
            // ====================================

            Text(
                text =
                    studentVerification
                        ?.extractedStudentSchoolName
                        ?: "Unknown School",

                color =
                    Color.White.copy(alpha = 0.9f)
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            // ====================================
            // TRUST SCORE
            // ====================================

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .background(
                        Color.White.copy(alpha = 0.18f)
                    )
                    .padding(

                        horizontal = 24.dp,

                        vertical = 12.dp
                    )
            ) {

                Text(
                    text =
                        "Trust Score: ${
                            userCore?.trustScore ?: 0
                        }",

                    color = Color.White,

                    style =
                        MaterialTheme.typography
                            .titleMedium,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}
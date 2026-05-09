package com.studentjobs.app.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.VerifiedUser
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
fun VerificationStatusCard(
    state: ProfileUiState
) {

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF111827),
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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = Color(0xFF4ADE80)
                )

                androidx.compose.foundation.layout.Spacer(
                    modifier = Modifier.size(10.dp)
                )

                Text(
                    text = "Verification Status",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            VerificationItem(
                title = "Student Identity",
                verified = state.isStudentVerified
            )

            Spacer(modifier = Modifier.height(14.dp))

            VerificationItem(
                title = "Phone Number",
                verified = state.isPhoneVerified
            )

            Spacer(modifier = Modifier.height(14.dp))

            VerificationItem(
                title = "Student Email",
                verified = state.isStudentEmailVerified
            )
        }
    }
}

@Composable
private fun VerificationItem(
    title: String,
    verified: Boolean
) {

    val verifiedColor =
        if (verified)
            Color(0xFF4ADE80)
        else
            Color(0xFF94A3B8)

    val verifiedText =
        if (verified)
            "Verified"
        else
            "Pending"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            color = Color.White.copy(alpha = 0.92f),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = verifiedColor
            )

            Spacer(modifier = Modifier.size(6.dp))

            Text(
                text = verifiedText,
                color = verifiedColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
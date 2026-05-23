package com.studentjobs.app.feature.profile.shared.components

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Public
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
fun ContactInfoCard(
    state: ProfileUiState
) {

    val userCore = state.userCore

    val studentVerification = state.studentVerification

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF111827), Color(0xFF1E293B)
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

            // ===== HEADER =====

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Call,

                    contentDescription = null,

                    tint = Color(0xFF22D3EE)
                )

                Spacer(
                    modifier = Modifier.height(0.dp)
                )

                Text(
                    text = " Contact Information",

                    color = Color.White,

                    style = MaterialTheme.typography.titleLarge,

                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // ===== PHONE =====

            ContactItem(
                icon = {

                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,

                        contentDescription = null,

                        tint = Color(0xFF8B5CF6)
                    )
                },

                label = "Phone Number",

                value = userCore?.phoneNumber
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // ===== STUDENT EMAIL =====

            ContactItem(
                icon = {

                    Icon(
                        imageVector = Icons.Default.Email,

                        contentDescription = null,

                        tint = Color(0xFF60A5FA)
                    )
                },

                label = "Student Email",

                value = state.studentProfile?.studentEmail
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // ===== LOGIN EMAIL =====

            ContactItem(
                icon = {

                    Icon(
                        imageVector = Icons.Default.Public,

                        contentDescription = null,

                        tint = Color(0xFF22C55E)
                    )
                },

                label = "Login Email",

                value = userCore?.loginEmail
            )
        }
    }
}

@Composable
private fun ContactItem(

    icon: @Composable () -> Unit,

    label: String,

    value: String?

) {

    Column {

        Text(
            text = label,

            color = Color.White.copy(alpha = 0.6f),

            style = MaterialTheme.typography.bodySmall
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            icon()

            Text(
                text = if (value.isNullOrBlank()) "Not Available"
                else value,

                color = Color.White,

                style = MaterialTheme.typography.bodyLarge,

                fontWeight = FontWeight.Medium
            )
        }
    }
}


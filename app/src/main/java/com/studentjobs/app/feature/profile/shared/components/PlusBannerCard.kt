package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole

@Composable
fun PlusBannerCard(

    currentPlan: SubscriptionPlan,

    role: UserRole,

    onUpgradePlusClick: () -> Unit

) {

    val gradient = Brush.horizontalGradient(

        colors = listOf(

            Color(0xFF6A11CB),

            Color(0xFF2575FC)
        )
    )

    val benefits = when (role) {

        UserRole.STUDENT ->

            "Auto Apply • OCR Timetable • Conflict Detection"

        UserRole.EMPLOYER ->

            "Auto Recruitment • Smart Filtering • Priority Boost"
    }

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),

        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Box(

            modifier = Modifier
                .background(gradient)
                .padding(24.dp)
        ) {

            Column {

                // ====================================
                // TOP ROW
                // ====================================

                Row(

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.AutoAwesome,

                        contentDescription = null,

                        tint = Color(0xFFFFD54F),

                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(

                            text = "StudentJobs PLUS",

                            style =
                                MaterialTheme.typography.titleLarge,

                            color = Color.White,

                            fontWeight = FontWeight.Bold
                        )

                        Text(

                            text =
                                "Unlock smart recruitment system",

                            color =
                                Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ====================================
                // BENEFITS
                // ====================================

                Text(

                    text = benefits,

                    color = Color.White,

                    style =
                        MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ====================================
                // STATUS / BUTTON
                // ====================================

                if (currentPlan == SubscriptionPlan.PLUS) {

                    AssistChip(

                        onClick = {},

                        label = {

                            Text(
                                text = "PLUS Activated"
                            )
                        },

                        leadingIcon = {

                            Icon(

                                imageVector =
                                    Icons.Default.Star,

                                contentDescription = null,

                                tint = Color(0xFFFFD54F)
                            )
                        },

                        colors =
                            AssistChipDefaults.assistChipColors(

                                containerColor =
                                    Color.White
                            )
                    )

                } else {

                    Button(

                        onClick = onUpgradePlusClick,

                        colors =
                            ButtonDefaults.buttonColors(

                                containerColor =
                                    Color(0xFFFFD54F)
                            ),

                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Text(

                            text = "Upgrade to PLUS",

                            color = Color.Black,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
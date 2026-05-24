package com.studentjobs.app.feature.subscription.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
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
fun SubscriptionHeader(

    currentPlan: SubscriptionPlan,

    role: UserRole

) {

    val gradient = Brush.horizontalGradient(

        colors = listOf(

            Color(0xFFFFD54F),

            Color(0xFFFFB300)
        )
    )

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(24.dp)
    ) {

        Column(

            modifier = Modifier
                .background(gradient)
                .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(

                imageVector = Icons.Default.Stars,

                contentDescription = null,

                tint = Color.White,

                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(

                text = "StudentJobs PLUS",

                style =
                    MaterialTheme.typography.headlineSmall,

                color = Color.White,

                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(

                text = if (
                    role == UserRole.STUDENT
                ) {
                    "Smart Auto Apply • OCR Thời khóa biểu • Conflict Detection"
                } else {
                    "Auto Recruitment • Smart Filtering • Priority Recommendation"
                },

                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            AssistChip(

                onClick = {},

                label = {

                    Text(
                        text =
                            "Current Plan: ${currentPlan.name}"
                    )
                }
            )
        }
    }
}
package com.studentjobs.app.feature.role.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RoleCard(
    title: String,
    description: String,
    selected: Boolean,
    activeColor: Color, // Màu sắc đặc trưng riêng khi Role này được chọn
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    // Viền đổi màu theo màu đặc trưng của từng Role
    val borderBrush = if (selected) {
        Brush.horizontalGradient(listOf(activeColor, activeColor.copy(alpha = 0.6f)))
    } else {
        Brush.linearGradient(listOf(Color(0x22FFFFFF), Color(0x22FFFFFF)))
    }

    val containerBgColor = if (selected) Color(0xFF1F2937).copy(alpha = 0.9f) else Color(0xFF111827).copy(alpha = 0.6f)
    val contentColor = if (selected) activeColor else Color.White
    val descriptionColor = if (selected) Color.White.copy(alpha = 0.9f) else Color.LightGray.copy(alpha = 0.7f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = if (selected) 2.dp else 1.dp,
                brush = borderBrush,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerBgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Box icon phát sáng nhẹ theo màu chủ đạo của Role đó
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        if (selected) activeColor.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    icon()
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = descriptionColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
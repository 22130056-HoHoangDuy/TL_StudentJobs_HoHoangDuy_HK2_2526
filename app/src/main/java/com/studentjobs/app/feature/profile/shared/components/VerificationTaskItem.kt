package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun VerificationTaskItem(
    title: String,
    isDone: Boolean,
    onClick: () -> Unit
) {
    // Đổi sang màu xanh lá mướt mắt đồng bộ với toàn bộ cụm xác thực trước đó
    val icon = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked
    val tintColor = if (isDone) Color(0xFF4ADE80) else Color(0xFF94A3B8)

    // Nếu xong rồi thì làm mờ chữ đi một chút để phân biệt với nhiệm vụ chưa làm
    val textColor = if (isDone) Color.White.copy(alpha = 0.6f) else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp) // Tăng nhẹ padding cho dễ bấm bằng ngón tay
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tintColor
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge, // Tăng size chữ lên một tí cho rõ ràng
            color = textColor,
            fontWeight = if (isDone) FontWeight.Normal else FontWeight.Medium
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.4f) // Làm mờ mũi tên chuyển màn cho sang
        )
    }
}
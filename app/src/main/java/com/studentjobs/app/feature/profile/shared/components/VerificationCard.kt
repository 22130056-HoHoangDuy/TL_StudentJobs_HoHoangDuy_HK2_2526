package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.status.VerificationStatus

@Composable
fun VerificationCard(
    title: String,
    description: String,
    status: VerificationStatus,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    // Phối lại bộ Icon, Màu sắc và Chữ hiển thị theo trạng thái (Chuẩn tiếng Việt đồ án)
    val (icon, color, statusText, buttonText) = when (status) {
        VerificationStatus.VERIFIED -> Quadruple(
            Icons.Default.CheckCircle,
            Color(0xFF4ADE80), // Xanh lá mượt
            "Đã xác thực thành công ✅",
            "Xem lại"
        )

        VerificationStatus.UNVERIFIED -> Quadruple(
            Icons.Default.Warning,
            Color(0xFF94A3B8), // Xám Slate
            "Chưa xác thực",
            "Xác thực"
        )

        VerificationStatus.PENDING -> Quadruple(
            Icons.Default.HourglassBottom,
            Color(0xFFFACC15), // Vàng hổ phách
            "Hồ sơ đang chờ duyệt...",
            "Chờ duyệt"
        )

        VerificationStatus.REJECTED -> Quadruple(
            Icons.Default.Warning,
            Color(0xFFF87171), // Đỏ Coral
            "Hồ sơ bị từ chối ❌",
            "Thử lại"
        )
    }

    // Logic kiểm tra xem có cho phép click hay không (Đang PENDING thì khóa lại)
    val isActionable = enabled && status != VerificationStatus.PENDING

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(enabled = isActionable) { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B) // Tone màu tối đồng bộ xịn mịn
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.65f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = statusText,
                    color = color,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Button hành động bên phải card
            Button(
                onClick = onClick,
                enabled = isActionable,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (status) {
                        VerificationStatus.VERIFIED -> Color(0xFF334155) // Màu tối trung tính cho nút xem lại
                        VerificationStatus.REJECTED -> Color(0xFFF87171) // Đỏ cho nút làm lại
                        else -> Color(0xFF06B6D4) // Xanh Cyan thương hiệu cho nút xác thực chính
                    },
                    contentColor = if (status == VerificationStatus.VERIFIED) Color.White else Color.Black,
                    disabledContainerColor = Color(0xFF1E293B).copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.4f)
                )
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * Data class phụ trợ chứa 4 tham số để gộp logic map trạng thái cho gọn code
 */
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
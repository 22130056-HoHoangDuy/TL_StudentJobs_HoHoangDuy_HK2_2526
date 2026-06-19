package com.studentjobs.app.feature.trust.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.data.model.trust.TrustLog
import java.text.SimpleDateFormat

@Composable
fun TrustHistoryCard(logs: List<TrustLog>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Lịch sử điểm uy tín",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (logs.isEmpty()) {
                Text(
                    text = "Chưa ghi nhận biến động điểm nào.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                logs.forEachIndexed { index, log ->
                    TrustLogItem(log)
                    if (index < logs.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color(0xFFF1F5F9),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrustLogItem(log: TrustLog) {
    val isPositive = log.changeAmount >= 0
    val pointColor = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444)
    val pointPrefix = if (isPositive) "+ " else ""

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cột bên trái: Chi tiết nội dung hành động
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formatAction(log.actionType),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155)
            )

            if (!log.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = log.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            val formattedDate =
                log.createdAt?.let {
                    SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        LocalLocale.current.platformLocale
                    ).format(it)
                } ?: "N/A"

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Cột bên phải: Biến động số điểm nhảy màu sắc bắt mắt
        Text(
            text = "$pointPrefix${log.changeAmount}",
            color = pointColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
    }
}

private fun formatAction(action: String): String {
    return when (action) {
        "EMAIL_VERIFIED" -> "Xác thực Email thành công"
        "PHONE_VERIFIED" -> "Xác thực Số điện thoại"
        "STUDENT_VERIFIED" -> "Xác thực Thẻ sinh viên"
        "EMPLOYER_VERIFIED" -> "Xác thực Doanh nghiệp"
        "JOB_COMPLETED" -> "Hoàn thành công việc xuất sắc"
        "JOB_CANCELLED" -> "Hủy lịch công việc đột xuất"
        else -> action
    }
}
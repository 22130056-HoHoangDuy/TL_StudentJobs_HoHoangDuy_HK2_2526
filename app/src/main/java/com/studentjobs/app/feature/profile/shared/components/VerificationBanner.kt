package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun VerificationBanner(
    title: String,
    subtitle: String
) {
    // Tạo hiệu ứng chuyển màu mượt mà để tiệp với giao diện chung của app
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF1E293B), // Xám xanh tối nhạt
            Color(0xFF334155)  // Xám xanh sáng hơn tí làm điểm nhấn
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Để lộ nền gradient bên dưới
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                // Sử dụng tham số title truyền vào để linh hoạt hiển thị câu Gen Z từ màn hình chính
                text = if (title.isBlank()) "Xác thực danh tính ngay đi nè! 🚀" else title,
                color = Color(0xFFFACC15), // Màu vàng nổi bật tinh tế trên nền tối
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                // Sử dụng tham số subtitle truyền vào
                text = if (subtitle.isBlank()) "Lên tích xanh chuẩn SV Real để tăng độ uy tín với nhà tuyển dụng nha." else subtitle,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
package com.studentjobs.app.feature.trust.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TrustGaugeCard(
    trustScore: Int,
    trustLevel: String
) {
    // Giới hạn điểm từ 0 đến 100 để tính toán tỉ lệ phần trăm
    val safeScore = trustScore.coerceIn(0, 100)
    val animatedSweepAngle by animateFloatAsState(
        targetValue = (safeScore / 100f) * 240f, // Vòng cung quét tối đa 240 độ
        animationSpec = tween(durationMillis = 1000),
        label = "GaugeAnimation"
    )

    // Tạo dải màu chuyển sắc giống phong cách MoMo (Tím hồng sang Hồng cam)
    val momoGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF6B21A8), Color(0xFFD946EF), Color(0xFFEC4899))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ĐIỂM UY TÍN TÀI KHOẢN",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                // Vẽ vòng đo tiến trình bằng Canvas
                Canvas(modifier = Modifier.size(180.dp)) {
                    // 1. Vẽ nền vòng cung màu xám mờ phía dưới
                    drawArc(
                        color = Color(0xFFF1F5F9),
                        startAngle = 150f,
                        sweepAngle = 240f,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                    // 2. Vẽ dải màu Gradient chạy theo tiến trình điểm thực tế
                    drawArc(
                        brush = momoGradient,
                        startAngle = 150f,
                        sweepAngle = animatedSweepAngle,
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Phần hiển thị chữ số ở trung tâm vòng cung
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = trustScore.toString(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 54.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Thẻ tag hiển thị danh hiệu xếp hạng (Ví dụ: Hạng Khỏe Mạnh)
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEFF6FF), shape = RoundedCornerShape(100.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🛡️ $trustLevel",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cập nhật ngày: ${
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                        Date()
                    )
                }",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
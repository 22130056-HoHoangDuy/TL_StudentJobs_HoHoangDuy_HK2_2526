package com.studentjobs.app.feature.profile.student.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.feature.profile.shared.components.VerificationTaskItem

@Composable
fun ProfileCompletionSection(
    isStudentVerified: Boolean,
    isPhoneVerified: Boolean,
    isEmailVerified: Boolean,
    isStudentEmailVerified: Boolean,
    onStudentClick: () -> Unit,
    onPhoneClick: () -> Unit,
    onEmailClick: () -> Unit
) {
    val total = 3
    val completed = listOf(
        isStudentVerified,
        isPhoneVerified,
        isEmailVerified
    ).count { it }

    val progress = completed / total.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "ProgressAnimation"
    )

    // Bảng nền LIGHT MODE Gen Z: Trắng loang dần sang Cyan và Hồng pastel siêu mượt
    val lightScreenGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFE0F2FE), // Xanh dương nhạt mát mắt
            Color(0xFFFCE7F3)  // Hồng pastel năng động
        )
    )

    // Thanh tiến độ màu cực đậm đà rực rỡ
    val progressGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFF43F5E), Color(0xFF06B6D4))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightScreenGradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Tiêu đề chữ Đậm đen cá tính, nổi bật 100%
            Text(
                text = "Nâng Cấp Uy Tín Bồ Ơi! ✨",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1B4B), // Tím than siêu đậm, cực kỳ nổi
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Hoàn thành các bước xác thực bên dưới\nđể tăng 100% cơ hội được các chủ quán chốt đơn nhé!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF4B5563), // Màu xám thanh lịch, dễ đọc
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // Khu vực hiển thị tiến độ (Light theme)
            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFE5E7EB)) // Nền thanh xám nhạt
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = animatedProgress)
                            .height(12.dp)
                            .background(progressGradient, RoundedCornerShape(6.dp))
                    )
                }

                Spacer(Modifier.width(16.dp))

                Text(
                    text = "$completed/$total",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0284C7)
                    )
                )
            }

            Spacer(Modifier.height(44.dp))

            // DANH SÁCH 3 MỤC QUAN TRỌNG: Tách ra từng Card trắng có shadow đổ bóng, chữ đen của anh sẽ nổi bần bật!
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Tạo khoảng cách thoáng đãng giữa các mục
            ) {

                // Card 1: Thẻ sinh viên
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.padding(4.dp)) {
                        VerificationTaskItem(
                            title = "Xác thực Thẻ Sinh Viên 🎓",
                            isDone = isStudentVerified,
                            onClick = onStudentClick
                        )
                    }
                }

                // Card 2: Số điện thoại
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.padding(4.dp)) {
                        VerificationTaskItem(
                            title = "Xác thực Số Điện Thoại 📱",
                            isDone = isPhoneVerified,
                            onClick = onPhoneClick
                        )
                    }
                }

                // Card 3: Email trường
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.padding(4.dp)) {
                        VerificationTaskItem(
                            title = "Xác thực Email Trường (.edu) ✉️",
                            isDone = isStudentEmailVerified,
                            onClick = onEmailClick
                        )
                    }
                }
            }
        }
    }
}
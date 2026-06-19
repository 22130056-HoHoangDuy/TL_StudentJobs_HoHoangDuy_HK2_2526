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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
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

    // Bảng nền Gen Z chuẩn đét: Tím chuyển sang Mint dịu mắt
    val lightScreenGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6366F1),
            Color(0xFFC7D2FE),
            Color(0xFFA7F3D0)
        )
    )

    // Thanh tiến độ màu Tím đậm rực rỡ
    val progressGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF4F46E5), Color(0xFF6366F1))
    )

    // Matrix đảo ngược màu sắc: Biến chữ trắng tàng hình thành chữ đen cá tính
    val invertMatrix = ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f, // Red
            0f, -1f, 0f, 0f, 255f, // Green
            0f, 0f, -1f, 0f, 255f, // Blue
            0f, 0f, 0f, 1f, 0f  // Alpha
        )
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

            // Tiêu đề chữ Đậm cá tính, nổi bần bật
            Text(
                text = "Nâng Cấp Uy Tín Bồ Ơi!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1B4B),
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Hoàn thành các bước xác thực bên dưới\nđể tăng 100% cơ hội được các chủ quán chốt đơn nhé!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF1F2937),
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // Khu vực hiển thị tiến độ
            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x40FFFFFF))
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
                        color = Color(0xFF1E1B4B)
                    )
                )
            }

            Spacer(Modifier.height(44.dp))

            // DANH SÁCH 3 MỤC QUAN TRỌNG: Giữ nguyên logic cấu trúc gốc
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Card 1: Thẻ sinh viên
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White) // Giữ nền Card trắng tinh khôi
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            // Tuyệt chiêu: Chỉ đảo ngược màu sắc của nội dung bên trong Component con (Trắng -> Đen)
                            .graphicsLayer {
                                colorFilter = ColorFilter.colorMatrix(invertMatrix)
                            }
                    ) {
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
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .graphicsLayer {
                                colorFilter = ColorFilter.colorMatrix(invertMatrix)
                            }
                    ) {
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
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .graphicsLayer {
                                colorFilter = ColorFilter.colorMatrix(invertMatrix)
                            }
                    ) {
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
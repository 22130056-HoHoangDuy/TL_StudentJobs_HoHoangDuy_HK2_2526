package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
            Color(0xFF7B2FF7),
            Color(0xFF3A7BFD)
        )
    )

    // Việt hóa các tính năng xịn sò theo từng Role cụ thể
    val benefits = when (role) {
        UserRole.STUDENT ->
            "Ứng Tuyển Tự Động • Quét Lịch Học OCR • Cảnh Báo Trùng Ca Học"

        UserRole.EMPLOYER ->
            "Tuyển Dụng Thông Minh • Lọc Ứng Viên VIP • Đẩy Tin Lên Xu Hướng"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                )
        ) {

            // ====================================
            // TOP
            // ====================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size(28.dp)
                )

                Column {
                    Text(
                        text = "StudentJobs PLUS",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Mở khóa hệ thống kết nối việc làm thông minh",
                        color = Color.White.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ====================================
            // BENEFITS
            // ====================================
            Text(
                text = benefits,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ====================================
            // BUTTON / STATUS
            // ====================================
            if (currentPlan == SubscriptionPlan.PLUS) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = "Đã Kích Hoạt PLUS",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7B2FF7) // Đổi màu chữ cho tiệp tone với màu tím gradient nền
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD54F)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White
                    )
                )
            } else {
                Button(
                    onClick = onUpgradePlusClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD54F)
                    )
                ) {
                    Text(
                        text = "Nâng Cấp PLUS Ngay",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun VerificationStatusCard(
    state: ProfileUiState
) {
    val studentVerification = state.studentVerification

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF111827), Color(0xFF1E1B4B)
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .padding(22.dp)
        ) {

            // ====================================
            // HEADER (Việt hóa)
            // ====================================
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = Color(0xFF4ADE80)
                )

                Spacer(modifier = Modifier.width(10.dp)) // Sửa .size sang .width cho chuẩn layout hàng ngang

                Text(
                    text = "Trạng thái xác thực",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ====================================
            // THẺ SINH VIÊN
            // ====================================
            VerificationItem(
                title = "Thẻ sinh viên chính chủ",
                verified = studentVerification?.studentCardVerified == VerificationStatus.VERIFIED
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ====================================
            // SỐ ĐIỆN THOẠI
            // ====================================
            VerificationItem(
                title = "Số điện thoại liên hệ",
                verified = studentVerification?.studentPhoneVerified == VerificationStatus.VERIFIED
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ====================================
            // EMAIL TRƯỜNG
            // ====================================
            VerificationItem(
                title = "Email sinh viên (.edu)",
                verified = studentVerification?.studentEmailVerified == VerificationStatus.VERIFIED
            )
        }
    }
}

@Composable
private fun VerificationItem(
    title: String,
    verified: Boolean
) {
    // Nếu verified = true thì xanh lá uy tín, ngược lại xám mờ chờ xử lý
    val verifiedColor = if (verified) Color(0xFF4ADE80) else Color(0xFF94A3B8)

    // Đổi chữ hiển thị sang tiếng Việt thân thiện, chuẩn chỉnh
    val verifiedText = if (verified) "Đã duyệt" else "Đang chờ..."

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.92f),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = verifiedColor
            )

            Spacer(modifier = Modifier.width(6.dp)) // Sửa .size sang .width

            Text(
                text = verifiedText,
                color = verifiedColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
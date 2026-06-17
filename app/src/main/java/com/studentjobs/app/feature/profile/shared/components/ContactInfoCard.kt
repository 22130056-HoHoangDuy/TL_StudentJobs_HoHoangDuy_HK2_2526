package com.studentjobs.app.feature.profile.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Public
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
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun ContactInfoCard(
    state: ProfileUiState
) {
    val userCore = state.userCore

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF111827), Color(0xFF1E293B)
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

            // ===== HEADER (Việt hóa + Sửa layout giãn cách) =====
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = Color(0xFF22D3EE)
                )

                Text(
                    text = "Thông tin liên hệ",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== SỐ ĐIỆN THOẠI =====
            ContactItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = Color(0xFF8B5CF6)
                    )
                },
                label = "Số điện thoại di động",
                value = userCore?.phoneNumber
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ===== EMAIL SINH VIÊN =====
            ContactItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFF60A5FA)
                    )
                },
                label = "Email sinh viên (.edu)",
                value = state.studentProfile?.studentEmail
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ===== EMAIL ĐĂNG NHẬP =====
            ContactItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = Color(0xFF22C55E)
                    )
                },
                label = "Email tài khoản hệ thống",
                value = userCore?.loginEmail
            )
        }
    }
}

@Composable
private fun ContactItem(
    icon: @Composable () -> Unit,
    label: String,
    value: String?
) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            icon()

            Text(
                text = if (value.isNullOrBlank()) "Chưa cập nhật rồi..." else value, // Vibe Gen Z nhẹ nhàng
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
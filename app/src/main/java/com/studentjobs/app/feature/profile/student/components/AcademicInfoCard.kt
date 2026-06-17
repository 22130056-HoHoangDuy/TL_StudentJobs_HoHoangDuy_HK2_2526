package com.studentjobs.app.feature.profile.student.components

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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun AcademicInfoCard(
    state: ProfileUiState,
    onUpdateClick: () -> Unit = {} // Thêm callback để xử lý khi bấm nút cập nhật
) {
    val studentProfile = state.studentProfile
    val studentVerification = state.studentVerification

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E293B)
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
            // HEADER (Việt hóa + Thêm nút Cập nhật)
            // ====================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = Color(0xFF60A5FA)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Thông tin học vấn",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Nút "Cập nhật" nhỏ gọn, tinh tế bằng Icon bút chì đúng chuẩn UI hiện đại
                IconButton(onClick = onUpdateClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Cập nhật thông tin",
                        tint = Color(0xFF60A5FA)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // MÃ SỐ SINH VIÊN
            AcademicItem(
                label = "Mã số sinh viên (MSSV)",
                value = studentVerification?.extractedStudentId ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TRƯỜNG ĐẠI HỌC
            AcademicItem(
                label = "Trường Đại học / Cao đẳng",
                value = studentVerification?.extractedStudentSchoolName ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            // EMAIL SINH VIÊN
            AcademicItem(
                label = "Email sinh viên (.edu)",
                value = studentProfile?.studentEmail ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            // NGÀY SINH
            AcademicItem(
                label = "Ngày tháng năm sinh",
                value = studentVerification?.extractedStudentDob ?: ""
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ====================================
            // VERIFIED FOOTER (Gen Z Style)
            // ====================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color(0xFF4ADE80)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Đã quét OCR - Real 100% nha!",
                    color = Color(0xFF4ADE80), // Đổi Color.0xFF... thành Color(0xFF...) là mượt ngay!
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun AcademicItem(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Badge,
                contentDescription = null,
                tint = Color(0xFF8B5CF6)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (value.isBlank()) "Trống trơn luôn..." else value, // Chuẩn Gen Z khi thiếu data
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
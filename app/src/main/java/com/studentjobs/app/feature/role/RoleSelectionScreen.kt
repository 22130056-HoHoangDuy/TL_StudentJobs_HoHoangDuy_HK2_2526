package com.studentjobs.app.feature.role

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.feature.role.components.RoleCard
import com.studentjobs.app.utils.AppPreferences

@Composable
fun RoleSelectionScreen(
    onContinue: (UserRole) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { AppPreferences(context) }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF1E1B4B), Color(0xFF111029), Color(0xFF0F172A))
    )

    // Nút bấm đổi màu thông minh: Chọn SV ra màu Cyan Gen Z, chọn Tuyển dụng ra màu Cam Cafe ấm áp
    val buttonGradient = when (selectedRole) {
        UserRole.STUDENT -> Brush.horizontalGradient(listOf(Color(0xFF06B6D4), Color(0xFF3B82F6)))
        UserRole.EMPLOYER -> Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFEF4444)))
        else -> Brush.horizontalGradient(listOf(Color.Gray, Color.Gray))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hệ sinh thái\ncủa bồ là... 🪐",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 40.sp,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Text(
                text = "Chọn đúng vai trò để mở khóa giao diện phù hợp nhất nhé!",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(36.dp))

            // ROLE 1: SINH VIÊN (Màu xanh neon năng động)
            RoleCard(
                title = "Sinh Viên Tìm Việc ⚡",
                description = "Kiếm job part-time Circle K, Jollibee, rạp phim... lịch làm linh hoạt, nhận lương nhanh.",
                selected = selectedRole == UserRole.STUDENT,
                activeColor = Color(0xFF06B6D4), // Cyan Gen Z
                icon = {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
            ) {
                selectedRole = UserRole.STUDENT
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ROLE 2: NHÀ TUYỂN DỤNG (Màu vàng cam F&B ấm áp, tin cậy)
            RoleCard(
                title = "Nhà Tuyển Dụng ☕",
                description = "Quản lý Quán Cafe, Nhà hàng, Cửa hàng tiện lợi... Đăng tin gọi đội, chốt nhân sự trong ngày.",
                selected = selectedRole == UserRole.EMPLOYER,
                activeColor = Color(0xFFF59E0B), // Amber/Orange ấm áp chuyên nghiệp
                icon = {
                    Icon(
                        imageVector = Icons.Default.LocalCafe, // Đổi sang icon ly cafe cho đúng chất F&B/Retail
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
            ) {
                selectedRole = UserRole.EMPLOYER
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    selectedRole?.let { role ->
                        prefs.saveUserRole(role.name)
                        onContinue(role)
                    }
                },
                enabled = selectedRole != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .then(
                        if (selectedRole != null) Modifier.background(
                            buttonGradient,
                            RoundedCornerShape(16.dp)
                        )
                        else Modifier
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Tiến vào app luôn 🚀",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (selectedRole != null) Color.White else Color.Gray
                    )
                )
            }
        }
    }
}
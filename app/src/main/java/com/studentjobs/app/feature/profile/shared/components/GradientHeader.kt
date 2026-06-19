package com.studentjobs.app.feature.profile.shared.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun GradientHeader(
    state: ProfileUiState
) {
    val userCore = state.userCore
    val studentProfile = state.studentProfile
    val studentVerification = state.studentVerification

    val context = LocalContext.current
    val sharedPreferences =
        remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

    // Khởi tạo state bằng cách đọc giá trị đã lưu trước đó (Duy nhất 1 chỗ này)
    var localAvatarUri by remember {
        mutableStateOf(sharedPreferences.getString("saved_avatar", null)?.let { Uri.parse(it) })
    }

    // Launcher kích hoạt mở Gallery (Duy nhất 1 chỗ này)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            localAvatarUri = uri
            // Lưu đường dẫn cố định vào SharedPreferences để debug không bị mất
            sharedPreferences.edit().putString("saved_avatar", uri.toString()).apply()
        }
    }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2563EB),
            Color(0xFF7C3AED),
            Color(0xFF06B6D4)
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

// AVATAR (Bản sửa lỗi không hiện ảnh)
// ====================================
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { photoPickerLauncher.launch("image/*") }
            ) {
                if (localAvatarUri != null || !studentProfile?.avatarUrl.isNullOrBlank()) {
                    // Nếu có ảnh local hoặc ảnh từ profile thì dùng Image để load
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = localAvatarUri ?: studentProfile?.avatarUrl
                        ),
                        contentDescription = "Avatar sinh viên",
                        modifier = Modifier
                            .size(120.dp)
                            .border(width = 4.dp, color = Color.White, shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Nếu trống trơn, ta dùng Icon mặc định của Android làm placeholder cực xịn
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color(0xFFE2E8F0)) // Nền xám nhạt chuẩn FB clone
                            .border(width = 4.dp, color = Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.AccountCircle,
                            contentDescription = "Default Avatar",
                            tint = Color(0xFF94A3B8), // Màu bóng người xám đậm hơn
                            modifier = Modifier.size(110.dp) // Kích thước bóng người
                        )
                    }
                }

                // Nút Camera nhỏ đè lên góc dưới avatar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF7C3AED), CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Đổi ảnh",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ====================================
            // NAME
            // ====================================
            Text(
                text = if (studentVerification?.extractedStudentName.isNullOrBlank()) {
                    studentProfile?.fullName ?: "Học muội ẩn danh"
                } else {
                    studentVerification?.extractedStudentName ?: "Học muội ẩn danh"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ====================================
            // VERIFIED
            // ====================================
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color(0xFF4ADE80)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Sinh viên chuẩn \"Real\"",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ====================================
            // SCHOOL
            // ====================================
            Text(
                text = studentVerification?.extractedStudentSchoolName ?: "Trường đời đào tạo",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ====================================
            // TRUST SCORE (Gen Z - Tinh chỉnh màu hiển thị rõ ràng)
            // ====================================
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.18f))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Điểm uy tín: ${userCore?.trustScore ?: 0}",
                    color = Color.White, // Để màu trắng cho nổi bật rõ ràng trên nền Gradient anh nhé!
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
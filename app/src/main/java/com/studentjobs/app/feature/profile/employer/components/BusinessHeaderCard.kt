package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.studentjobs.app.feature.profile.ProfileUiState

@Composable
fun BusinessHeaderCard(
    state: ProfileUiState,
    onEditAvatar: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val profile = state.employerProfile
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF6366F1), Color(0xFFA855F7), Color(0xFFEC4899))
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .background(gradient)
                    .padding(top = 54.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar tương tác click để đổi ảnh trực tiếp
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.clickable { onEditAvatar() }
                ) {
                    // ... trong BusinessHeaderCard.kt ...

                    AsyncImage(
                        // 🔥 Cập nhật dòng này: Ưu tiên ảnh tạm trong RAM trước
                        model = profile?.tempAvatarUri?.ifBlank { null }
                            ?: profile?.businessLogoUrl,
                        contentDescription = "Business Logo",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.White, CircleShape)
                    )
                    // Badge bút chì nhỏ góc avatar báo hiệu đổi được ảnh
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(28.dp)
                            .border(2.dp, Color.White, CircleShape)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Edit,
                                null,
                                modifier = Modifier.size(12.dp),
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = profile?.businessName ?: "Chưa cập nhật",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = profile?.businessCategory ?: "Doanh nghiệp",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 🔥 THÊM NÚT SETTINGS chuẩn phong cách như của Student tuyển dụng
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Cài đặt", tint = Color.White)
        }
    }
}
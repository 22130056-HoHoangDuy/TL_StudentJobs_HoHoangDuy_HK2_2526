package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    onEditHeader: () -> Unit
) {
    val profile = state.employerProfile
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6366F1),
            Color(0xFFA855F7),
            Color(0xFFEC4899)
        ) // Màu sắc rực rỡ hơn
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
                    .padding(top = 40.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar với viền Neon
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = profile?.businessLogoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.White, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = profile?.businessName ?: "Chưa cập nhật",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )

                // Chip danh mục kiểu GenZ
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = CircleShape,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp), // Set kích thước ở Modifier nha anh
                            tint = Color(0xFF22C55E)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            profile?.businessCategory ?: "Category",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        // Nút Edit Header nổi
        IconButton(
            onClick = onEditHeader,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
        }
    }
}

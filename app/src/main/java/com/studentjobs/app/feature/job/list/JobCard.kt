package com.studentjobs.app.feature.job.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.data.model.job.JobEntity

@Composable
fun JobCard(
    job: JobEntity,
    distanceKm: Double?,
    onClick: () -> Unit = {},

    ) {
    val brandIndigo = Color(0xFF4F46E5)
    val brandPink = Color(0xFF0D81EC)
    val textDark = Color(0xFF0F172A)
    val textMuted = Color(0xFF64748B)
    val salaryTeal = Color(0xFF0D9488)
    val tagBg = Color(0xFFF1F5F9)

    val displayCategory = job.businessCategory.ifBlank { "Part-time" }
    val firstChar = job.title.firstOrNull()?.toString() ?: "J"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(185.dp) // 1. ÉP CỨNG CHIỀU CAO: Giúp tất cả các Card trong danh sách đều tăm tắp như nhau
            .padding(horizontal = 16.dp, vertical = 6.dp),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(), // 2. LẤY HẾT DIỆN TÍCH: Bắt buộc Row phải nở hết khung của Card
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // CỘT TRÁI: Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(colors = listOf(brandIndigo, brandPink)),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = firstChar.uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // CỘT PHẢI: Thông tin chữ
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), // 3. CHIẾM HẾT CHIỀU CAO CÒN LẠI để kích hoạt tính năng đẩy đáy
                verticalArrangement = Arrangement.spacedBy(4.dp) // Thu hẹp khoảng cách để không bị tràn khung
            ) {
                // Dòng 1: Tiêu đề & Lương
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = job.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = textDark,
                            fontSize = 15.sp // Giảm nhẹ 1 size giúp text tiêu đề an toàn khi có từ dài
                        ),
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${formatSalary(job.salaryMin)}-${formatSalary(job.salaryMax)}/h", // Viết tắt đ/h cho gọn gàng, vừa vặn khung ngang
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = salaryTeal
                        )
                    )
                }

                // Dòng 2: Danh mục
                Text(
                    text = displayCategory,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = brandIndigo
                    )
                )

                // Dòng 3: Địa điểm & Khoảng cách (Gom chung vào cụm để layout không bị rời rạc)
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = "Location",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = job.locationText,
                            style = MaterialTheme.typography.bodySmall,
                            color = textMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text =
                            if (distanceKm != null)
                                "📍 Cách bạn %.1f km".format(distanceKm)
                            else
                                "📍 Chưa xác định",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = brandPink.copy(alpha = 0.85f),
                        modifier = Modifier.padding(start = 18.dp) // Đã xóa "hộp" đi rồi nha haha
                    )
                }

                // Dòng 4: Kỹ năng (FlowRow tự động xuống hàng cực mượt)
                if (job.requiredSkills.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp)
                    ) {
                        job.requiredSkills.take(3).forEach { skill ->
                            Box(
                                modifier = Modifier
                                    .background(color = tagBg, shape = CircleShape)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = skill,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color(0xFF475569)
                                )
                            }
                        }
                    }
                }

                // ==========================================
                // 4. CHIÊU CHÍ MẠNG: LÒ XO ĐẨY ĐÁY
                Spacer(modifier = Modifier.weight(1f))
                // ==========================================

                // Dòng cuối: Nút chuyển trang (Luôn nằm cố định ở đáy dù text ở trên dài hay ngắn)
                Text(
                    text = "Xem chi tiết →",
                    color = brandIndigo,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.End) // Đẩy chữ sang bên phải nhìn cho thuận mắt theo luồng đọc
                )
            }
        }
    }
}

private fun formatSalary(salary: Double): String {
    val amount = salary.toInt()
    return if (amount >= 1000) "${amount / 1000}k" else "$amount"
}
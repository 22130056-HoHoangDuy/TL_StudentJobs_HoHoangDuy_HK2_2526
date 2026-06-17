// File: com/studentjobs.app/feature/trust/components/TrustOverviewCard.kt
package com.studentjobs.app.feature.trust.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrustOverviewCard() {
    // Trạng thái đóng mở riêng biệt cho phần giới thiệu chung và từng mục lớn
    var isMainExpanded by remember { mutableStateOf(false) }
    var isPositiveExpanded by remember { mutableStateOf(false) }
    var isNegativeExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 20.dp)
                .animateContentSize( // Tự động kích hoạt hiệu ứng co giãn mượt mà khi chiều cao thay đổi
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
        ) {
            // ====================================================
            // 1. TIÊU ĐỀ CHÍNH & GIỚI THIỆU CHUNG (CÓ THỂ THU GỌN)
            // ====================================================
            ExpandableHeader(
                title = "📖 Cẩm nang Điểm uy tín",
                isExpanded = isMainExpanded,
                onToggle = { isMainExpanded = !isMainExpanded },
                titleColor = Color(0xFF1E293B),
                isMainHeader = true
            )

            if (isMainExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Điểm uy tín phản ánh mức độ nghiêm túc và tin cậy của bạn trong cộng đồng StudentJobs. Chỉ số này quyết định trực tiếp đến quyền lợi và trải nghiệm của bạn trên nền tảng.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B),
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color(0xFFF1F5F9),
                thickness = 1.dp
            )

            // ====================================================
            // 2. MỤC: LÀM SAO ĐỂ TÍCH LŨY ĐIỂM
            // ====================================================
            ExpandableHeader(
                title = "🎯 Làm sao để tích lũy điểm?",
                isExpanded = isPositiveExpanded,
                onToggle = { isPositiveExpanded = !isPositiveExpanded },
                titleColor = Color(0xFF334155)
            )

            if (isPositiveExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hệ thống sẽ cộng điểm khi tài khoản hoàn tất các bước xác thực thông tin chính chủ (Số điện thoại, Email, Thẻ sinh viên hoặc Giấy phép doanh nghiệp) với mức cố định +10 điểm cho mỗi danh mục (áp dụng một lần duy nhất). Ngoài ra, việc luôn hoàn thành các công việc và hợp đồng đúng cam kết sẽ giúp điểm số của bạn tăng trưởng bền vững.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF475569),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color(0xFFF1F5F9),
                thickness = 1.dp
            )

            // ====================================================
            // 3. MỤC: CÁC HÀNH VI GÂY GIẢM ĐIỂM
            // ====================================================
            ExpandableHeader(
                title = "⚠️ Các hành vi gây giảm điểm",
                isExpanded = isNegativeExpanded,
                onToggle = { isNegativeExpanded = !isNegativeExpanded },
                titleColor = Color(0xFF334155)
            )

            if (isNegativeExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Điểm số sẽ bị khấu trừ nghiêm khắc nếu người dùng vi phạm các điều khoản cam kết: Tự ý hủy lịch công việc đột xuất sát giờ, không đến nơi làm việc mà không có lý do chính đáng, hoặc đăng tải các thông tin tuyển dụng không chính xác.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF475569),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun ExpandableHeader(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    titleColor: Color,
    isMainHeader: Boolean = false
) {
    // Tạo hiệu ứng xoay icon mũi tên mượt góc 180 độ khi đóng/mở
    val arrowRotateAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "ArrowRotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onToggle() } // Cho phép bấm vào bất kỳ vị trí nào trên dòng tiêu đề
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = if (isMainHeader) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = "Toggle Section",
            tint = Color(0xFF94A3B8),
            modifier = Modifier.rotate(arrowRotateAngle) // Áp dụng góc quay của hiệu ứng động
        )
    }
}
package com.studentjobs.app.feature.application.student.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.data.model.application.ApplicationEntity
import com.studentjobs.app.data.model.application.ApplicationStatus

@Composable
fun ApplicationCard(
    application: ApplicationEntity,
    isWorkingTab: Boolean
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Hàng tiêu đề: Tên công việc & Badge Trạng thái
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = application.jobTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🏢 ${application.businessName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )
                }

                // Trạng thái Badge phong cách MoMo
                val (badgeBg, badgeText, statusLabel) = when (application.status) {
                    ApplicationStatus.PENDING.name -> Triple(
                        Color(0xFFFEF3C7),
                        Color(0xFFD97706),
                        "Chờ duyệt"
                    )

                    ApplicationStatus.ACCEPTED.name -> Triple(
                        Color(0xFFE0F2FE),
                        Color(0xFF0369A1),
                        "Đã nhận"
                    )

                    ApplicationStatus.WORKING.name -> Triple(
                        Color(0xFFDCFCE7),
                        Color(0xFF15803D),
                        "Đang làm"
                    )

                    else -> Triple(Color(0xFFF1F5F9), Color(0xFF475569), application.status)
                }

                Box(
                    modifier = Modifier
                        .background(badgeBg, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = badgeText
                    )
                }
            }

            // Nếu thuộc mục "Đang làm việc", hiển thị khu vực tương tác Action Buttons
            if (isWorkingTab) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // NÚT BÁO CÁO (Tạm thời chưa gắn logic nghiệp vụ)
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Tính năng báo cáo đang được phát triển",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                        modifier = Modifier.height(38.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Report",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Báo cáo", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // NÚT LIÊN HỆ (Kích hoạt Intent gọi điện hệ thống Android)
                    OutlinedButton(
                        onClick = {
                            // Giả định đối tượng application có chứa trường số điện thoại employerPhone,
                            // Nếu không có, hệ thống sẽ fallback về một chuỗi trống hoặc số tổng đài mặc định
                            val phoneNumber = application.employerPhone ?: ""
                            if (phoneNumber.isNotEmpty()) {
                                launchDialer(context, phoneNumber)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Nhà tuyển dụng chưa cập nhật số điện thoại",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFEFF6FF),
                            contentColor = Color(0xFF2563EB)
                        ),
                        modifier = Modifier.height(38.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Liên hệ", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Hàm điều hướng nhảy sang màn hình gọi điện của thiết bị
private fun launchDialer(context: Context, phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Không thể mở ứng dụng cuộc gọi", Toast.LENGTH_SHORT).show()
    }
}
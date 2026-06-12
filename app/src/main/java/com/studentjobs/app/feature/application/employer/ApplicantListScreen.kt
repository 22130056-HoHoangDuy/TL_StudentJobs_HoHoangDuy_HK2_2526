package com.studentjobs.app.feature.application.employer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.data.model.application.ApplicantItem

// Định nghĩa bảng màu Gen Z (Anh có thể đưa vào Theme.kt)
val BrandPurple = Color(0xFF6366F1) // Indigo/Purple trẻ trung
val BrandPink = Color(0xFFF43F5E)   // Rose rực rỡ
val SoftGreen = Color(0xFF10B981)   // Emerald nhẹ dịu cho nút Nhận
val GhostGray = Color(0xFFF8FAFC)   // Nền sáng tinh tế

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantListScreen(
    viewModel: ApplicantListViewModel
) {
    var selectedApplicant by remember { mutableStateOf<ApplicantItem?>(null) }
    val state by viewModel.uiState.collectAsState()

    // Sử dụng Scaffold để layout chuẩn chỉnh và chuyên nghiệp hơn
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ứng viên đang đợi ⚡",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = GhostGray
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.applicants.isEmpty()) {
                // Giao diện khi trống trống trải (Empty State)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Chưa có ai ứng tuyển, kiên nhẫn tí nha! 🥺", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các card tự động
                ) {
                    items(state.applicants) { applicant ->
                        ApplicantCard(

                            applicant = applicant,

                            onDetailClick = {
                                selectedApplicant = applicant
                            },

                            onAcceptClick = {

                                viewModel.acceptApplicant(
                                    applicant.application
                                )
                            },

                            onRejectClick = {

                                viewModel.rejectApplicant(
                                    applicant.application.applicationId
                                )
                            }
                        )
                    }
                }
            }

            // [FIX LỖI CORNER CASE]: Đưa AlertDialog RA NGOÀI LazyColumn để tránh lỗi render
            selectedApplicant?.let { applicant ->
                ApplicantDetailDialog(
                    applicant = applicant,
                    onDismiss = { selectedApplicant = null }
                )
            }
        }
    }
}

@Composable
fun ApplicantCard(
    applicant: ApplicantItem,
    onDetailClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    val application = applicant.application
    val profile = applicant.studentProfile
    val user = applicant.userCore

    val fullName = profile?.fullName ?: application.studentName
    val firstChar = fullName.firstOrNull()?.toString() ?: "S"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailClick() }, // Ấn vào nguyên Card cũng xem được chi tiết
        shape = RoundedCornerShape(20.dp), // Bo góc mượt mà đúng chuẩn Gen Z
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Row 1: Avatar chữ + Tên & Trường + Badge Xác thực
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar được tạo bằng Gradient màu sắc năng động
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(BrandPurple, Color(0xFFFFA5F3))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = firstChar.uppercase(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // ❌ LỖI CŨ: Column(modifier = Modifier.weight(1.dp)) {
//          |- Do truyền nhầm đơn vị .dp vào hàm weight

//  SỬA LẠI THÀNH:
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = fullName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                        if (user?.userVerified == true) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = BrandPurple,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = profile?.schoolName ?: "Chưa cập nhật trường",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                        maxLines = 1
                    )
                }

                // Điểm uy tín (Trust Score) biến thành Badge Ngôi sao
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(BrandPurple.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Trust Score",
                            tint = BrandPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${user?.trustScore ?: 0}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = BrandPurple,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Row 2: Nút hành động nhanh (Từ chối / Nhận)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BrandPink
                    ),
                    border = BorderStroke(
                        1.dp,
                        BrandPink.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        "Từ chối",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onAcceptClick,
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftGreen
                    )
                ) {
                    Text(
                        "Chấp nhận",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ApplicantDetailDialog(
    applicant: ApplicantItem,
    onDismiss: () -> Unit
) {
    val profile = applicant.studentProfile
    val user = applicant.userCore

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = profile?.fullName ?: "Thông tin ứng viên",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoRow(label = "📧 Email", value = profile?.studentEmail ?: "N/A")
                InfoRow(label = "🏫 Trường", value = profile?.schoolName ?: "N/A")
                InfoRow(label = "🎓 Ngành", value = profile?.major ?: "N/A")
                InfoRow(label = "📞 Số điện thoại", value = user?.phoneNumber ?: "N/A")

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Trạng thái profile:", color = Color.Gray)
                    Text(
                        text = if (user?.userVerified == true) "Đã xác thực ✨" else "Chưa xác thực ⚠️",
                        fontWeight = FontWeight.Bold,
                        color = if (user?.userVerified == true) BrandPurple else BrandPink
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đóng", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}
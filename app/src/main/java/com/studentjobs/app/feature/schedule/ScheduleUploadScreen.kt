package com.studentjobs.app.feature.schedule

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.feature.subscription.components.SubscriptionTopBar

@Composable
fun ScheduleUploadScreen(
    navController: NavController, // Thêm NavController để điều hướng quay lại
    viewModel: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser

    // ========================================
    // KÍCH HOẠT TRÌNH CHỌN ẢNH TỪ THƯ VIỆN
    // ========================================
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.selectImage(it) }
    }

    Scaffold(
        topBar = {
            // Tích hợp thanh TopBar đồng bộ, có sẵn nút Back
            SubscriptionTopBar(
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = Color(0xFF0F172A) // Ép cứng nền tối sang xịn mịn
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()), // Chống tràn màn hình khi hiện preview ảnh to
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tải Lên Thời Khóa Biểu",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ========================================
            // BANNER HƯỚNG DẪN (ĐÃ ĐỘ DARK MODE)
            // ========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B) // Đổi từ xanh nhạt cũ sang xám Slate tối
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Hãy chọn ảnh chụp lịch học rõ nét để kích hoạt tính năng Rải CV thông minh.",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Hệ thống AI (Python OCR) sẽ tự động bóc tách dữ liệu và cảnh báo nếu công việc bạn ứng tuyển bị trùng giờ học trên lớp.",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ========================================
            // KHU VỰC HIỂN THỊ ẢNH PREVIEW
            // ========================================
            if (uiState.selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(uiState.selectedImageUri),
                    contentDescription = "Ảnh thời khóa biểu đã chọn",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1E293B)),
                    contentScale = ContentScale.Fit // Đổi sang Fit để sinh viên nhìn trọn vẹn được tấm ảnh lịch học không bị mất chữ
                )
            } else {
                // Hiển thị khung trống nếu chưa chọn ảnh cho UI bớt đơn điệu
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Chưa có ảnh nào được chọn",
                            color = Color.White.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ========================================
            // NÚT CHỌN ẢNH (PICK IMAGE)
            // ========================================
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF334155) // Tone màu xám sang xịn không tranh chấp với nút submit chính
                )
            ) {
                Text(
                    text = if (uiState.selectedImageUri == null) "Chọn Ảnh Thời Khóa Biểu" else "Thay Đổi Ảnh Khác",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ========================================
            // NÚT PHÂN TÍCH & TẢI LÊN (UPLOAD)
            // ========================================
            val isUploadEnabled = uiState.selectedImageUri != null && !uiState.isLoading

            Button(
                onClick = {
                    currentUser?.uid?.let { viewModel.uploadTimetable(it) }
                },
                enabled = isUploadEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF06B6D4), // Xanh cyan công nghệ rực rỡ
                    disabledContainerColor = Color(0xFF1E293B).copy(alpha = 0.4f),
                    disabledContentColor = Color.White.copy(alpha = 0.3f)
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Bắt Đầu Phân Tích Lịch Học",
                        color = if (isUploadEnabled) Color.White else Color.White.copy(alpha = 0.3f),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ========================================
            // THÔNG BÁO KẾT QUẢ TRẠNG THÁI (SUCCESS / ERROR)
            // ========================================
            uiState.successMessage?.let {
                Text(
                    text = "🎉 $it",
                    color = Color(0xFF4ADE80), // Xanh neon mượt mắt
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            uiState.errorMessage?.let {
                Text(
                    text = "❌ $it",
                    color = Color(0xFFF87171), // Đỏ Coral nổi bật chống lỗi màu
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
    LaunchedEffect(
        uiState.successMessage
    ) {

        if (
            uiState.successMessage != null
        ) {

            navController.popBackStack()
        }
    }
}
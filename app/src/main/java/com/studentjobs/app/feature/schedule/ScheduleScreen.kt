package com.studentjobs.app.feature.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.feature.schedule.components.ScheduleCard
import com.studentjobs.app.feature.subscription.components.SubscriptionTopBar

@Composable
fun ScheduleScreen(
    navController: NavController,
    currentPlan: SubscriptionPlan,
    viewModel: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(uid) {
        if (uid != null) {
            viewModel.observeSchedule(uid)
        }
    }

    Scaffold(
        topBar = {
            SubscriptionTopBar(
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->

        // ========================================
        // 1. TRƯỜNG HỢP USER CHƯA LÊN VIP (FREE USER)
        // ========================================
        if (currentPlan != SubscriptionPlan.PLUS) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tính năng tải thời khóa biểu OCR chỉ dành riêng cho tài khoản hội viên PLUS ⭐",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nâng cấp ngay để AI tự bóc tách lịch học và giúp bạn né bẫy trùng ca làm việc nhé!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate("subscription") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Nâng Cấp Hội Viên PLUS",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            return@Scaffold
        }

        // ========================================
        // 2. TRẠNG THÁI ĐANG LOAD MẠNG (LOADING)
        // ========================================
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF06B6D4),
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "AI đang quét thời khóa biểu của bạn...",
                    color = Color(0xFF06B6D4),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            return@Scaffold
        }

        // ========================================
        // 3. TRẠNG THÁI ĐANG XỬ LÝ OCR (PROCESSING)
        // ========================================
        if (uiState.isProcessingOcr) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.size(56.dp),
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Hệ thống đang phân tích thời khóa biểu...",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Chờ tí nhé, quét bằng AI siêu tốc trong vài giây thui!",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            return@Scaffold
        }

        // ========================================
        // 4. TRẠNG THÁI TRỐNG (EMPTY STATE)
        // ========================================
        if (uiState.schedule == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Trống lịch học rồi nè! 📅",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Hãy tải ảnh chụp thời khóa biểu từ trường của bạn lên. Hệ thống AI sẽ tự động bóc tách để kích hoạt tính năng Cảnh báo trùng ca làm việc khi tìm việc part-time.",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate("schedule_upload") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06B6D4))
                ) {
                    Text(
                        text = "Tải Thời Khóa Biểu Lên Ngay",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            return@Scaffold
        }

        // ========================================
        // 5. HIỂN THỊ LỊCH HỌC KHI ĐÃ CÓ DỮ LIỆU
        // ========================================
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ScheduleCard(schedule = uiState.schedule!!)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
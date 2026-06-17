package com.studentjobs.app.feature.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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

    // ========================================
    // 🔥 FIX LỖI GIẬT MÀN HÌNH CHÍNH Ở ĐÂY 🔥
    // Bọc trong LaunchedEffect để chỉ chạy duy nhất 1 lần khi màn hình khởi tạo
    // ========================================
    LaunchedEffect(uid) {
        if (uid != null) {
            viewModel.loadSchedule(uid)
        }
    }

    Scaffold(
        topBar = {
            SubscriptionTopBar(
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = Color(0xFF0F172A) // Ép tone tối Cyberpunk đồng bộ hệ thống
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
                Text(
                    text = "Tính năng tải thời khóa biểu OCR chỉ dành riêng cho tài khoản hội viên PLUS ⭐",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("subscription") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB300) // Vàng Gold nổi bật
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Nhận diện lịch học", color = Color.Black, fontWeight = FontWeight.Bold)
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
                    color = Color(0xFF06B6D4), // Xanh cyan thương hiệu
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "AI đang xử lý thời khóa biểu của bạn...",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            return@Scaffold
        }

        // ========================================
        // 3. TRẠNG THÁI TRỐNG (EMPTY STATE)
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
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E293B) // Card xám tối đồng bộ
                    )
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Chưa có dữ liệu lịch học 📅",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Hãy tải ảnh chụp thời khóa biểu từ trường của bạn lên. Hệ thống sẽ tự động bóc tách để kích hoạt tính năng Cảnh báo trùng ca làm việc khi tìm việc.",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("schedule_upload") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF06B6D4)
                    )
                ) {
                    Text(
                        "Tải Thời Khóa Biểu Lên",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            return@Scaffold
        }

        // ========================================
        // 4. HIỂN THỊ LỊCH HỌC KHI ĐÃ CÓ DỮ LIỆU
        // ========================================
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ScheduleCard(schedule = uiState.schedule!!)
            }
        }
    }
}
package com.studentjobs.app.feature.subscription.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Nâng cấp PLUS ngay", // Việt hóa tiêu đề ngắn gọn, rõ ràng mục đích màn hình
                color = Color.White, // Ép cứng chữ màu trắng để không bị lẫn vào nền
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White // Ép nút quay lại màu trắng đồng bộ
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent, // Để trong suốt nhằm ôm trọn nền Gradient của Header phía sau
            scrolledContainerColor = Color(0xFF0F172A) // Khi cuộn màn hình lên, đổi thành màu tối để không lộ text phía dưới
        )
    )
}
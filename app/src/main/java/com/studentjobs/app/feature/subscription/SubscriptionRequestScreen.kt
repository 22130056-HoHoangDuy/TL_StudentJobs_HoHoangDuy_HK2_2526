package com.studentjobs.app.feature.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.subscription.SubscriptionRequest
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.feature.subscription.components.SubscriptionTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionRequestScreen(
    role: UserRole,
    onBackClick: () -> Unit,
    onRequestSuccess: () -> Unit,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedDuration by remember { mutableIntStateOf(30) }

    // Cấu hình giá tiền chuẩn đồ án
    val monthlyPrice = when (role) {
        UserRole.STUDENT -> 9000
        UserRole.EMPLOYER -> 29000
    }

    // Tính tổng tiền động (Có áp dụng chiết khấu giảm giá cho gói 3 tháng)
    val amount = when (selectedDuration) {
        30 -> monthlyPrice
        90 -> when (role) {
            UserRole.STUDENT -> 25000     // Giảm từ 27k xuống 25k (Rẻ hơn thật!)
            UserRole.EMPLOYER -> 79000    // Giảm từ 87k xuống 79k
        }

        else -> monthlyPrice
    }

    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
    val paymentContent = "SJPLUS_${uid.take(6)}_${selectedDuration}D"

    // URL sinh mã VietQR chuẩn xác theo ngân hàng BIDV của anh Duy
    val qrUrl = "https://img.vietqr.io/image/BIDV-3144423183-compact2.png" +
            "?amount=$amount" +
            "&addInfo=$paymentContent" +
            "&accountName=HO%20HOANG%20DUY"

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            onRequestSuccess()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            // Tái sử dụng lại chính TopBar cao cấp không lỗi màu anh em mình vừa làm
            SubscriptionTopBar(onBackClick = onBackClick)
        },
        containerColor = Color(0xFF0F172A) // Ép toàn bộ nền màn hình sang màu Dark Mode đồng bộ
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(4.dp))

            // ====================================
            // 1. BANNER TIÊU ĐỀ GÓI VIP (HEADER CARD)
            // ====================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "StudentJobs PLUS",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (role == UserRole.STUDENT) {
                            "Đặc quyền: Rải CV Tự Động • Quét Lịch Học OCR • Cảnh Báo Trùng Ca"
                        } else {
                            "Đặc quyền: Tuyển Dụng AI • Lọc Ứng Viên VIP • Gợi Ý Ưu Tiên"
                        },
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // ====================================
            // 2. CỤM CHỌN THỜI HẠN GÓI (PLAN OPTIONS)
            // ====================================
            Text(
                text = "Chọn thời hạn đăng ký",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            listOf(30, 90).forEach { days ->
                val isSelected = selectedDuration == days
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF1E293B).copy(
                            alpha = 0.6f
                        )
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFFFB300)
                    ) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Gói Hội Viên $days Ngày",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (days == 30) "Trải nghiệm tính năng cơ bản" else "Tiết kiệm hơn • Khuyên dùng 🔥",
                                color = if (days == 30) Color.White.copy(alpha = 0.6f) else Color(
                                    0xFF4ADE80
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedDuration = days },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFFFB300),
                                unselectedColor = Color.Gray
                            )
                        )
                    }
                }
            }

            // ====================================
            // 3. THẺ THANH TOÁN VIETQR CHUYỂN KHOẢN
            // ====================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Quét mã QR để thanh toán",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ảnh mã QR tự động bo góc cực nét
                    AsyncImage(
                        model = qrUrl,
                        contentDescription = "Mã VietQR Chuyển Khoản",
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Số tiền cần thanh toán",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = String.format(
                            "%,d VNĐ",
                            amount
                        ), // Định dạng dấu phẩy phân tách hàng nghìn (Ví dụ: 27,000 VNĐ)
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFB300)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Nội dung chuyển khoản chuẩn",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = paymentContent,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF38BDF8), // Màu xanh neon nổi bật dễ sao chép
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "(Vui lòng giữ nguyên nội dung này khi chuyển khoản)",
                        color = Color.White.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ====================================
            // 4. NÚT XÁC NHẬN ĐÃ CHUYỂN KHOẢN (SUBMIT)
            // ====================================
            Button(
                onClick = {
                    val currentUid = FirebaseAuth.getInstance().currentUser?.uid
                    if (currentUid != null) {
                        viewModel.createSubscriptionRequest(
                            SubscriptionRequest(
                                userUid = currentUid,
                                durationDays = selectedDuration,
                                paymentMethod = "QR",
                                paymentAmount = amount,
                                paymentContent = paymentContent
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB300),
                    disabledContainerColor = Color(0xFF1E293B)
                ),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Xác nhận đã chuyển khoản",
                        color = Color.Black, // Chữ đen trên nền vàng nhìn cực kỳ nổi bật rõ ràng
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
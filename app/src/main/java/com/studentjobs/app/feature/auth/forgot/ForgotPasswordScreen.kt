package com.studentjobs.app.feature.auth.forgot

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentjobs.app.utils.UiState

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit
) {
    val viewModel: ForgotPasswordViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current // Sửa lỗi Toast Crash tại đây

    var email by remember { mutableStateOf("") }

    // Trạng thái kiểm soát hiệu ứng loading và khóa nút bấm
    val isLoading = state is UiState.Loading

    LaunchedEffect(state) {
        when (state) {
            is UiState.Success<*> -> {
                Toast.makeText(
                    context, // Đã thay 'null' bằng context xịn
                    "Đã gửi email khôi phục mật khẩu thành công!",
                    Toast.LENGTH_LONG
                ).show()
                // Gửi thành công có thể tự động back về màn đăng nhập luôn nếu muốn
                onBackClick()
            }

            is UiState.Error -> {
                // Tiện tay handle luôn lỗi nếu API trả về thất bại (ví dụ: email không tồn tại)
                Toast.makeText(
                    context,
                    "Có lỗi xảy ra, vui lòng kiểm tra lại email.",
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Quên mật khẩu?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nhập email sinh viên hoặc email nhà tuyển dụng của bạn để nhận liên kết thiết lập lại mật khẩu mới.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Ô nhập Email chuẩn form, bo góc mềm mại giống màn MoMo
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Địa chỉ Email") },
            placeholder = { Text("example@student.edu.vn") },
            singleLine = true,
            enabled = !isLoading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD946EF), // Đi theo tone hồng/tím branding của anh
                unfocusedBorderColor = Color(0xFFE2E8F0)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nút gửi có hiệu ứng Loading chặn spam-click
        Button(
            onClick = { if (email.isNotBlank()) viewModel.sendResetEmail(email) },
            enabled = !isLoading && email.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB), // Xanh Royal khỏe khoắn giống Tab Việc làm
                disabledContainerColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Tăng nhẹ chiều cao nút cho dễ bấm
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Gửi email khôi phục",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút quay lại tinh tế màu xám trầm slate
        TextButton(
            onClick = onBackClick,
            enabled = !isLoading
        ) {
            Text(
                text = "Quay lại đăng nhập",
                color = Color(0xFF64748B),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
package com.studentjobs.app.feature.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.utils.UiState

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) } // Trạng thái ẩn/hiện mật khẩu

    val state by viewModel.loginState.collectAsState()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF1E1B4B), Color(0xFF311042), Color(0xFF4C0519))
    )
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .align(Alignment.Center)
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111827).copy(alpha = 0.85f))
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chào mừng trở lại 👋",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "Kiếm tiền thôi bồ ơi! 💸",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email của bạn") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFEC4899),
                        unfocusedBorderColor = Color(0xFF374151),
                        focusedLabelColor = Color(0xFFEC4899),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLeadingIconColor = Color(0xFFEC4899)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật mã bảo mật") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    // Thêm icon con mắt bật/tắt ẩn hiện
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                                tint = if (isPasswordVisible) Color(0xFFEC4899) else Color.Gray
                            )
                        }
                    },
                    // Đổi kiểu hiển thị dựa theo state
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFEC4899),
                        unfocusedBorderColor = Color(0xFF374151),
                        focusedLabelColor = Color(0xFFEC4899),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLeadingIconColor = Color(0xFFEC4899)
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))
                TextButton(
                    onClick = onForgotPasswordClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Quên mật khẩu?",
                        color = Color(0xFFD946EF), // Màu hồng tím chuẩn MoMo, nổi bật vừa đủ
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(buttonGradient, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Đăng nhập ngay 🚀",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onNavigateToRegister,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Chưa có tài khoản? Tạo ngay nha ✨",
                        color = Color(0xFF67E8F9),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val s = state) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            color = Color(0xFFEC4899),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    is UiState.Error -> {
                        // Bộ lọc việt hóa lỗi dựa vào từ khóa tiếng Anh từ Server trả về
                        val errorRaw = s.message.lowercase()
                        val friendlyMessage = when {
                            errorRaw.contains("wrong-password") || errorRaw.contains("wrong password") || errorRaw.contains(
                                "invalid-credential"
                            ) || errorRaw.contains("invalid email") -> {
                                "Hình như sai email hoặc mật khẩu mất tiêu rồi bồ ơi! 🧐"
                            }

                            errorRaw.contains("user-not-found") || errorRaw.contains("no user") -> {
                                "Tài khoản này chưa tồn tại rồi, check lại hoặc tạo mới nha! ✨"
                            }

                            errorRaw.contains("invalid-email") || errorRaw.contains("badly formatted") -> {
                                "Email này nhìn lạ quá, gõ đúng định dạng hộ em nhé! 🥺"
                            }

                            errorRaw.contains("too-many-requests") || errorRaw.contains("blocked") || errorRaw.contains(
                                "attempts"
                            ) -> {
                                "Thử sai nhiều quá bị sập nguồn rồi! Đợi xíu rồi thử lại nha bồ. 🛑"
                            }

                            errorRaw.contains("network") || errorRaw.contains("timeout") || errorRaw.contains(
                                "unable to resolve"
                            ) -> {
                                "Mạng mẽo bất ổn quá, check lại Wi-Fi/4G hộ em với! 🌐"
                            }

                            else -> s.message // Nếu có lỗi lạ khác thì hiện gốc của server
                        }

                        Text(
                            text = friendlyMessage,
                            color = Color(0xFFF87171),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    is UiState.Success<*> -> {

                        LaunchedEffect(Unit) {

                            onLoginSuccess()

                            viewModel.resetLoginState()
                        }
                    }

                    else -> {}
                }
            }
        }
        LaunchedEffect(Unit) {
            Log.d("LOGIN", "SCREEN OPENED")
        }
    }
}
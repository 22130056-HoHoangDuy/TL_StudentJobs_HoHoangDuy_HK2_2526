package com.studentjobs.app.feature.auth

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.utils.AppPreferences
import com.studentjobs.app.utils.UiState

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: (UserCore) -> Unit
) {
    val context = LocalContext.current

    val role = remember {
        AppPreferences(context)
            .getUserRole()
            ?.let { UserRole.valueOf(it) }
    }

    val state by viewModel.registerState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // Dòng xác nhận mật khẩu mới

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) } // Lưu lỗi validate cục bộ

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF1E1B4B), Color(0xFF311042), Color(0xFF4C0519))
    )
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))
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
                    text = "Tạo tài khoản 🚀",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "Gia nhập hội hệ đi làm nào! 🔥",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Ô nhập Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email đăng ký") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF06B6D4),
                        unfocusedBorderColor = Color(0xFF374151),
                        focusedLabelColor = Color(0xFF06B6D4),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLeadingIconColor = Color(0xFF06B6D4)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ô nhập Mật khẩu chính
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        localError = null // Reset lỗi khi user gõ lại
                    },
                    label = { Text("Mật khẩu") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = if (isPasswordVisible) Color(0xFF06B6D4) else Color.Gray
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF06B6D4),
                        unfocusedBorderColor = Color(0xFF374151),
                        focusedLabelColor = Color(0xFF06B6D4),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLeadingIconColor = Color(0xFF06B6D4)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ô nhập Xác nhận mật khẩu
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        localError = null // Reset lỗi khi user gõ lại
                    },
                    label = { Text("Xác nhận lại mật khẩu") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            isConfirmPasswordVisible = !isConfirmPasswordVisible
                        }) {
                            Icon(
                                imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = if (isConfirmPasswordVisible) Color(0xFF06B6D4) else Color.Gray
                            )
                        }
                    },
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF06B6D4),
                        unfocusedBorderColor = Color(0xFF374151),
                        focusedLabelColor = Color(0xFF06B6D4),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLeadingIconColor = Color(0xFF06B6D4)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Nút Đăng ký kèm logic kiểm tra khớp mật khẩu
                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            localError = "Mật khẩu xác nhận không khớp nè bồ ơi! 🔍"
                        } else if (role != null) {
                            localError = null
                            viewModel.register(email, password, role)
                        }
                    },
                    enabled = role != null && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .then(
                            if (role != null) Modifier.background(
                                buttonGradient,
                                RoundedCornerShape(16.dp)
                            )
                            else Modifier
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Bắt đầu ngay thôi ⚡",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (role != null) Color.White else Color.DarkGray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị lỗi local (Lỗi lệch mật khẩu) trước, nếu không có thì hiển thị lỗi từ Api/State
                if (localError != null) {
                    Text(
                        localError!!,
                        color = Color(0xFFF87171),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    when (val s = state) {
                        is UiState.Loading -> CircularProgressIndicator(
                            color = Color(0xFF06B6D4),
                            modifier = Modifier.size(36.dp)
                        )

                        is UiState.Error -> Text(
                            s.message,
                            color = Color(0xFFF87171),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        is UiState.Success<*> -> {
                            val user = s.data as? UserCore
                            if (user != null) {
                                LaunchedEffect(user.uid) {
                                    onRegisterSuccess(user)
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
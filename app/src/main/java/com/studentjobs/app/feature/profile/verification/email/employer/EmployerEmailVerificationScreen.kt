package com.studentjobs.app.feature.profile.verification.email.employer

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.functions.FirebaseFunctions
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.repository.trust.TrustRepository
import com.studentjobs.app.firebase.firestore.TrustService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerEmailVerificationScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val functions = remember { FirebaseFunctions.getInstance("us-east1") }

    val trustRepository = remember {
        TrustRepository(TrustService(), UserServiceNew())
    }

    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isOtpSent by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color(0xFF1E293B)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8FAFC))
            )
        },
        containerColor = Color(0xFFF8FAFC) // Tone nền đồng bộ toàn bộ hệ thống Profile Employer
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Xác thực Email Doanh nghiệp",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (!isOtpSent)
                    "Vui lòng cung cấp địa chỉ Email chính thức của doanh nghiệp để nhận mã xác thực liên kết cấu hình tài khoản."
                else
                    "Mã xác thực OTP đã được gửi đến hòm thư $email. Vui lòng kiểm tra kỹ hộp thư đến hoặc thư rác (Spam).",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // INPUT EMAIL FIELD (THIẾT KẾ BO TRÒN 12DP HIỆN ĐẠI)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() }, // Tự động loại bỏ dấu cách thừa
                label = { Text("Email Doanh nghiệp") },
                placeholder = { Text("contact@company.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB), // Đảm bảo đồng bộ xanh tuyển dụng
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isOtpSent && !isLoading
            )

            // INPUT OTP FIELD
            if (isOtpSent) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = otp,
                    onValueChange = { if (it.length <= 6) otp = it.trim() },
                    label = { Text("Mã xác thực OTP") },
                    placeholder = { Text("******") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // BUTTON KÍCH HOẠT ĐỒNG BỘ
            Button(
                onClick = {
                    scope.launch {
                        error = ""
                        val user = auth.currentUser
                        if (user == null) {
                            error = "Không tìm thấy thông tin tài khoản nhà tuyển dụng"
                            return@launch
                        }

                        if (!isOtpSent) {
                            // ========================================
                            // LUỒNG 1: GỬI MÃ OTP ĐẾN EMAIL DOANH NGHIỆP
                            // ========================================
                            try {
                                if (!email.contains("@") || email.length < 5) {
                                    error = "Định dạng email doanh nghiệp không hợp lệ"
                                    return@launch
                                }

                                val isTemporaryEmail = email.endsWith("@tempmail.com") ||
                                        email.endsWith("@10minutemail.com")
                                if (isTemporaryEmail) {
                                    error = "Hệ thống không cho phép sử dụng email tạm thời"
                                    return@launch
                                }

                                isLoading = true

                                val data = hashMapOf("email" to email, "uid" to user.uid)
                                functions.getHttpsCallable("sendVerificationOtp").call(data).await()

                                isLoading = false
                                isOtpSent = true
                                Toast.makeText(
                                    context,
                                    "Mã OTP đã được gửi thành công! 📬",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                isLoading = false
                                error = e.message ?: "Quá trình gửi mã OTP gặp lỗi"
                            }
                        } else {
                            // ========================================
                            // LUỒNG 2: KIỂM TRA MÃ OTP ĐỂ XÁC THỰC
                            // ========================================
                            try {
                                if (otp.length < 4) {
                                    error = "Vui lòng nhập đầy đủ mã số OTP"
                                    return@launch
                                }

                                isLoading = true

                                val otpDoc =
                                    firestore.collection("email_otps").document(user.uid).get()
                                        .await()
                                val savedOtp = otpDoc.getString("otp")
                                val savedEmail = otpDoc.getString("email")

                                val verificationBeforeUpdate =
                                    firestore.collection("employer_verifications")
                                        .document(user.uid)
                                        .get()
                                        .await()

                                val wasEmailVerified =
                                    verificationBeforeUpdate.getString("businessEmailVerified") ==
                                            VerificationStatus.VERIFIED.name

                                if (savedOtp == otp && savedEmail == email) {
                                    firestore.collection("employer_verifications")
                                        .document(user.uid)
                                        .set(
                                            mapOf(
                                                "businessEmailVerified" to VerificationStatus.VERIFIED.name,
                                                "updatedAt" to Date()
                                            ),
                                            SetOptions.merge()
                                        )
                                        .await()

                                    if (!wasEmailVerified) {
                                        trustRepository.addTrustEvent(
                                            uid = user.uid,
                                            actionType = "EMPLOYER_EMAIL_VERIFIED",
                                            changeAmount = 10,
                                            description = "Xác thực email doanh nghiệp"
                                        )
                                    }

                                    // Dọn dẹp OTP sau khi xác thực thành công
                                    firestore.collection("email_otps")
                                        .document(user.uid)
                                        .delete()
                                        .await()

                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Xác thực Email Doanh nghiệp thành công! 🎉",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.popBackStack()
                                } else {
                                    isLoading = false
                                    error = "Mã OTP hoặc Email xác thực không chính xác"
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                error = e.message ?: "Xác thực mã OTP thất bại"
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB),
                    disabledContainerColor = Color(0xFFCBD5E1)
                ),
                enabled = !isLoading && (email.isNotEmpty() && (!isOtpSent || otp.isNotEmpty()))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = if (!isOtpSent) "Gửi mã OTP" else "Xác thực OTP",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // HIỂN THỊ LỖI PHÂN TRẬN CĂN GIỮA ĐẸP MẮT
            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "⚠️ $error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
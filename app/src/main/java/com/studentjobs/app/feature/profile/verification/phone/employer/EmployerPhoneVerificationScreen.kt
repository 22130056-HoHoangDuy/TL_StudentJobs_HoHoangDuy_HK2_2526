package com.studentjobs.app.feature.profile.verification.phone.employer

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.studentjobs.app.data.model.employer.EmployerVerification
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.utils.calculateTrustScore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerPhoneVerificationScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val formattedPhone = remember(phone) {
        when {
            phone.startsWith("0") -> "+84${phone.drop(1)}"
            phone.startsWith("+84") -> phone
            else -> "+84$phone"
        }
    }

    // Tối ưu hóa IO Coroutine: Tránh Re-fetching lặp lại, cập nhật đồng bộ 1 lần duy nhất
    suspend fun updateEmployerPhoneVerification(uid: String, phoneNum: String) {
        val verificationDocRef = firestore.collection("employer_verifications").document(uid)
        val userDocRef = firestore.collection("users").document(uid)

        // 1. Cập nhật cục bộ dữ liệu Phone trước trên máy chủ
        verificationDocRef.set(
            mapOf(
                "businessPhoneVerified" to VerificationStatus.VERIFIED,
                "updatedAt" to Date()
            ),
            SetOptions.merge()
        ).await()

        // 2. Tải song song thông tin để tránh độ trễ bất đồng bộ dữ liệu mạng
        val userSnapshot = userDocRef.get().await()
        val verificationSnapshot = verificationDocRef.get().await()

        val user = userSnapshot.toObject(UserCore::class.java)
        val employerVerification = verificationSnapshot.toObject(EmployerVerification::class.java)

        if (user != null) {
            // 3. Tính toán chính xác điểm uy tín dựa trên dữ liệu snapshot vừa hứng
            val trustScore =
                calculateTrustScore(user = user, employerVerification = employerVerification)

            val isAllVerified =
                employerVerification?.businessEmailVerified == VerificationStatus.VERIFIED
                        && employerVerification.businessPhoneVerified == VerificationStatus.VERIFIED
                        && employerVerification.businessLicenseVerified == VerificationStatus.VERIFIED

            // 4. Đồng bộ ngược dữ liệu Core sạch sẽ
            userDocRef.set(
                mapOf(
                    "phoneNumber" to phoneNum,
                    "trustScore" to trustScore,
                    "userVerified" to isAllVerified
                ),
                SetOptions.merge()
            ).await()
        }
    }

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
        containerColor = Color(0xFFF8FAFC) // Đồng bộ nền xám dịu mắt toàn diện hệ thống
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Xác thực SĐT Doanh nghiệp",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (!isCodeSent)
                    "Vui lòng nhập số điện thoại Hotline của doanh nghiệp để nhận mã xác thực OTP thiết lập cấu hình."
                else
                    "Mã xác thực gồm 6 số đã được gửi tới số $phone. Vui lòng kiểm tra tin nhắn SMS.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Ô NHẬP SỐ ĐIỆN THOẠI (UI TRỰC QUAN BO TRÒN)
            OutlinedTextField(
                value = phone,
                onValueChange = { if (it.all { char -> char.isDigit() }) phone = it },
                label = { Text("Số điện thoại Hotline") },
                placeholder = { Text("0912345678") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB), // Xanh Blue Công nghệ nhà tuyển dụng
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCodeSent && !isLoading
            )

            // Ô NHẬP OTP XUẤT HIỆN MƯỢT MÀ
            if (isCodeSent) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = otp,
                    onValueChange = {
                        if (it.length <= 6 && it.all { char -> char.isDigit() }) otp = it
                    },
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

            // PHÍM ĐIỀU KHIỂN CHÍNH (BO GÓC 12DP CHUẨN XỊN)
            Button(
                onClick = {
                    error = ""
                    if (!isCodeSent) {
                        if (phone.length < 9) {
                            error = "Số điện thoại không hợp lệ (Yêu cầu nhập từ 9 số trở lên)"
                            return@Button
                        }

                        isLoading = true
                        val options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(formattedPhone)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(context as Activity)
                            .setCallbacks(object :
                                PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                    scope.launch {
                                        try {
                                            auth.currentUser?.updatePhoneNumber(credential)?.await()
                                            val uid = auth.currentUser?.uid ?: return@launch
                                            updateEmployerPhoneVerification(uid, formattedPhone)
                                            isLoading = false
                                            Toast.makeText(
                                                context,
                                                "Xác thực số điện thoại thành công! 🎉",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack()
                                        } catch (e: Exception) {
                                            isLoading = false
                                            error = e.message
                                                ?: "Tự động kích hoạt số điện thoại thất bại"
                                        }
                                    }
                                }

                                override fun onVerificationFailed(e: FirebaseException) {
                                    isLoading = false
                                    error = e.message ?: "Quá trình gửi tin nhắn xác thực gặp lỗi"
                                }

                                override fun onCodeSent(
                                    id: String,
                                    token: PhoneAuthProvider.ForceResendingToken
                                ) {
                                    verificationId = id
                                    isCodeSent = true
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Mã OTP đã gửi đến thiết bị doanh nghiệp!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }).build()

                        PhoneAuthProvider.verifyPhoneNumber(options)
                    } else {
                        if (otp.length < 6) {
                            error = "Mã xác thực OTP phải chứa đúng 6 ký tự số"
                            return@Button
                        }

                        isLoading = true
                        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

                        auth.currentUser?.updatePhoneNumber(credential)
                            ?.addOnCompleteListener { task ->
                                val success =
                                    task.isSuccessful || task.exception?.message?.contains("already been linked") == true
                                scope.launch {
                                    if (success) {
                                        try {
                                            val uid = auth.currentUser?.uid ?: return@launch
                                            updateEmployerPhoneVerification(uid, formattedPhone)
                                            isLoading = false
                                            Toast.makeText(
                                                context,
                                                "Xác thực tài khoản doanh nghiệp thành công! 🚀",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack()
                                        } catch (e: Exception) {
                                            isLoading = false
                                            error = e.message
                                                ?: "Lỗi đồng bộ dữ liệu xác thực Doanh nghiệp"
                                        }
                                    } else {
                                        isLoading = false
                                        error = task.exception?.message
                                            ?: "Mã xác thực OTP không chính xác hoặc đã hết hạn"
                                    }
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
                enabled = !isLoading && (phone.isNotEmpty() && (!isCodeSent || otp.isNotEmpty()))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = if (!isCodeSent) "Gửi mã OTP" else "Xác thực OTP",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // PANEL THÔNG BÁO LỖI (DÀN TRẬN TEXT ĐỎ ĐẸP, RÕ RÀNG)
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
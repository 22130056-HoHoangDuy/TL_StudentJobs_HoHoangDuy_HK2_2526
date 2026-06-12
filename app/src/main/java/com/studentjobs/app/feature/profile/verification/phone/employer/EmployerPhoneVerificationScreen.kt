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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import java.util.concurrent.TimeUnit

@Composable
fun EmployerPhoneVerificationScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Firebase Instantiation bọc trong remember để tránh khởi tạo lại khi Re-composition
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    // States quản lý trạng thái UI
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // Định dạng số điện thoại chuẩn quốc tế tự động cập nhật theo biến phone
    val formattedPhone = remember(phone) {
        when {
            phone.startsWith("0") -> "+84${phone.drop(1)}"
            phone.startsWith("+84") -> phone
            else -> "+84$phone"
        }
    }

    // Hàm cập nhật trạng thái xác thực trên Firebase DB tập trung
    suspend fun updateEmployerPhoneVerification(uid: String, phoneNum: String) {
        // 1. Cập nhật trạng thái xác thực phone cho nhà tuyển dụng
        firestore.collection("employer_verifications").document(uid).set(
            mapOf(
                "businessPhoneVerified" to VerificationStatus.VERIFIED,
                "updatedAt" to System.currentTimeMillis()
            ),
            SetOptions.merge()
        ).await()

        // 2. Load thông tin mới nhất để chuẩn bị tính lại điểm uy tín
        val user =
            firestore.collection("users").document(uid).get().await().toObject(UserCore::class.java)
        val employerVerification =
            firestore.collection("employer_verifications").document(uid).get().await()
                .toObject(EmployerVerification::class.java)

        // 3. Tính toán lại điểm uy tín (Trust Score)
        val trustScore =
            calculateTrustScore(user = user!!, employerVerification = employerVerification)

        // 4. Kiểm tra điều kiện tổng hợp: Toàn bộ thông tin doanh nghiệp được verify sạch sẽ chưa
        val isAllVerified =
            employerVerification?.businessEmailVerified == VerificationStatus.VERIFIED
                    && employerVerification.businessPhoneVerified == VerificationStatus.VERIFIED
                    && employerVerification.businessLicenseVerified == VerificationStatus.VERIFIED

        // 5. Cập nhật đồng bộ ngược về nhánh User Core chính
        firestore.collection("users").document(uid).set(
            mapOf(
                "phoneNumber" to phoneNum,
                "trustScore" to trustScore,
                "userVerified" to isAllVerified
            ),
            SetOptions.merge()
        ).await()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Xác thực SĐT Doanh nghiệp",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // INPUT PHONE FIELD
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Số điện thoại doanh nghiệp") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isCodeSent && !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // INPUT OTP FIELD
        if (isCodeSent) {
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Mã xác thực OTP") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // BUTTON CONTROL LOGIC
        Button(
            onClick = {
                error = ""

                if (!isCodeSent) {
                    // ========================================
                    // LUỒNG 1: ĐĂNG KÝ VÀ GỬI MÃ SMS OTP
                    // ========================================
                    if (phone.length < 9) {
                        error = "Số điện thoại không hợp lệ (Yêu cầu từ 9 số trở lên)"
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
                                // Tự động điền/xác thực không cần nhập mã nếu thiết bị hỗ trợ đọc SMS tự động
                                scope.launch {
                                    try {
                                        auth.currentUser?.updatePhoneNumber(credential)?.await()
                                        val uid = auth.currentUser?.uid ?: return@launch
                                        updateEmployerPhoneVerification(uid, formattedPhone)

                                        isLoading = false
                                        Toast.makeText(
                                            context,
                                            "Xác thực số điện thoại thành công!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack()
                                    } catch (e: Exception) {
                                        isLoading = false
                                        error =
                                            e.message ?: "Tự động kích hoạt số điện thoại thất bại"
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
                                    "Mã OTP đã gửi đến thiết bị của doanh nghiệp!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }).build()

                    PhoneAuthProvider.verifyPhoneNumber(options)

                } else {
                    // ========================================
                    // LUỒNG 2: XÁC THỰC MÃ OTP DO USER NHẬP TRỰC TIẾP
                    // ========================================
                    if (otp.length < 6) {
                        error = "Mã xác thực OTP phải chứa đúng 6 ký tự"
                        return@Button
                    }

                    isLoading = true
                    val credential = PhoneAuthProvider.getCredential(verificationId, otp)

                    auth.currentUser?.updatePhoneNumber(credential)?.addOnCompleteListener { task ->
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
                                        "Xác thực tài khoản doanh nghiệp thành công!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    isLoading = false
                                    error = e.message ?: "Lỗi đồng bộ dữ liệu xác thực Doanh nghiệp"
                                }
                            } else {
                                isLoading = false
                                error = task.exception?.message ?: "Mã xác thực OTP không chính xác"
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (!isCodeSent) "Gửi mã OTP" else "Xác thực OTP")
            }
        }

        // ERROR MESSAGE DISPLAY (Xuất hiện mượt mà dưới nút bấm)
        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
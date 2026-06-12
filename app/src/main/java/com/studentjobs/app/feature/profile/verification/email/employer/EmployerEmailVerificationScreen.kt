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
import androidx.compose.foundation.verticalScroll
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

@Composable
fun EmployerEmailVerificationScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Firebase Instantiation bọc trong remember để tối ưu hiệu năng
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val functions = remember { FirebaseFunctions.getInstance("us-east1") }
    val trustRepository = remember {

        TrustRepository(

            TrustService(),

            UserServiceNew()
        )
    }

    // States quản lý UI
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isOtpSent by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Xác thực Email Doanh nghiệp",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // INPUT EMAIL FIELD
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Doanh nghiệp (Business Email)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isOtpSent && !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // INPUT OTP FIELD (Chỉ xuất hiện khi mã OTP đã được gửi đi)
        if (isOtpSent) {
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
                            if (!email.contains("@")) {
                                error = "Định dạng email doanh nghiệp không hợp lệ"
                                return@launch
                            }

                            // Chặn các đầu email ảo / rác đăng ký tài khoản doanh nghiệp
                            val isTemporaryEmail =
                                email.endsWith("@tempmail.com") || email.endsWith("@10minutemail.com")
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
                                "Mã OTP đã được gửi thành công!",
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
                            isLoading = true

                            val otpDoc =
                                firestore.collection("email_otps").document(user.uid).get().await()
                            val savedOtp = otpDoc.getString("otp")
                            val savedEmail = otpDoc.getString("email")

                            val verificationBeforeUpdate =

                                firestore.collection("employer_verifications")
                                    .document(user.uid)
                                    .get()
                                    .await()

                            val wasEmailVerified =

                                verificationBeforeUpdate
                                    .getString("businessEmailVerified") ==

                                        VerificationStatus.VERIFIED.name
                            if (savedOtp == otp && savedEmail == email) {
                                // Cập nhật trạng thái xác thực và tích hợp merge tránh đè mất các field verification khác
                                firestore.collection("employer_verifications")
                                    .document(user.uid)
                                    .set(
                                        mapOf(
                                            "businessEmailVerified" to VerificationStatus.VERIFIED.name,
                                            "updatedAt" to System.currentTimeMillis()
                                        ),
                                        SetOptions.merge()
                                    )
                                    .await()
                                if (!wasEmailVerified) {

                                    trustRepository.addTrustEvent(

                                        uid = user.uid,

                                        actionType =
                                            "EMPLOYER_EMAIL_VERIFIED",

                                        changeAmount = 10,

                                        description =
                                            "Xác thực email doanh nghiệp"
                                    )
                                }
                                firestore.collection("email_otps")
                                    .document(user.uid)
                                    .delete()
                                    .await()

                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Xác thực Email Doanh nghiệp thành công!",
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
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (!isOtpSent) "Gửi mã OTP" else "Xác thực OTP")
            }
        }

        // ERROR MESSAGE DISPLAY (Đặt gọn gàng dưới Button)
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
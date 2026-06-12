package com.studentjobs.app.feature.profile.verification.email.student

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.data.repository.trust.TrustRepository
import com.studentjobs.app.firebase.firestore.SchoolDomainService
import com.studentjobs.app.firebase.firestore.TrustService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun StudentEmailVerificationScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Firebase Instantiation
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val functions = remember { FirebaseFunctions.getInstance("us-east1") }
    val schoolDomainService = remember { SchoolDomainService() }
    val trustRepository = remember { TrustRepository(TrustService(), UserServiceNew()) }

    // States
    var studentEmail by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isOtpSent by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Xác thực Email Sinh viên",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // INPUT EMAIL FIELD
        OutlinedTextField(
            value = studentEmail,
            onValueChange = { studentEmail = it },
            label = { Text("Email Sinh viên") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isOtpSent && !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // INPUT OTP FIELD (Chỉ hiện sau khi gửi OTP thành công)
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
                        error = "Không tìm thấy thông tin tài khoản"
                        return@launch
                    }

                    if (!isOtpSent) {
                        // ========================================
                        // LUỒNG 1: GỬI OTP
                        // ========================================
                        try {
                            if (!studentEmail.contains("@")) {
                                error = "Định dạng email không hợp lệ"
                                return@launch
                            }

                            val domain = studentEmail.substringAfter("@")
                            isLoading = true

                            val isValid = schoolDomainService.isValidStudentDomain(domain)
                            if (!isValid) {
                                isLoading = false
                                error = "Email không thuộc danh sách tên miền trường học liên kết"
                                return@launch
                            }

                            val data = hashMapOf("email" to studentEmail, "uid" to user.uid)
                            functions.getHttpsCallable("sendVerificationOtp").call(data).await()

                            isLoading = false
                            isOtpSent = true
                            Toast.makeText(
                                context,
                                "Mã OTP đã được gửi vào email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            isLoading = false
                            error = e.message ?: "Gửi mã OTP thất bại"
                        }
                    } else {
                        // ========================================
                        // LUỒNG 2: XÁC THỰC MÃ OTP
                        // ========================================
                        try {
                            isLoading = true

                            val otpDoc =
                                firestore.collection("email_otps").document(user.uid).get().await()
                            val savedOtp = otpDoc.getString("otp")
                            val savedEmail = otpDoc.getString("email")

                            if (savedOtp == otp && savedEmail == studentEmail) {
                                val verificationBeforeUpdate =

                                    firestore.collection("student_verifications")
                                        .document(user.uid)
                                        .get()
                                        .await()
                                        .toObject(StudentVerification::class.java)

                                val wasEmailVerified =

                                    verificationBeforeUpdate?.studentEmailVerified ==
                                            VerificationStatus.VERIFIED

                                // 1. Cập nhật trạng thái xác thực email
                                firestore.collection("student_verifications").document(user.uid)
                                    .update(mapOf("studentEmailVerified" to VerificationStatus.VERIFIED))
                                    .await()

                                // 2. Đồng bộ email vào thông tin chi tiết sinh viên
                                firestore.collection("students").document(user.uid)
                                    .update(mapOf("studentEmail" to studentEmail))
                                    .await()

                                // 3. Đọc dữ liệu mới nhất kiểm tra trạng thái xác thực tổng hợp
                                val studentVerification =
                                    firestore.collection("student_verifications")
                                        .document(user.uid).get().await()
                                        .toObject(StudentVerification::class.java)

                                val isAllVerified =
                                    studentVerification?.studentEmailVerified == VerificationStatus.VERIFIED
                                            && studentVerification.studentPhoneVerified == VerificationStatus.VERIFIED
                                            && studentVerification.studentCardVerified == VerificationStatus.VERIFIED

                                // 4. Cập nhật trạng thái Verified cuối cùng lên User Core
                                firestore.collection("users").document(user.uid)
                                    .update(mapOf("userVerified" to isAllVerified))
                                    .await()

                                // 5. Thêm sự kiện cộng điểm uy tín (Trust Score) - CHỈ GỌI 1 LẦN DUY NHẤT
                                if (!wasEmailVerified) {

                                    trustRepository.addTrustEvent(
                                        uid = user.uid,
                                        actionType = "EMAIL_VERIFIED",
                                        changeAmount = 10,
                                        description = "Xác thực thành công email sinh viên"
                                    )
                                }

                                // OTP chỉ xóa khi verify thành công
                                firestore.collection("email_otps")
                                    .document(user.uid)
                                    .delete()
                                    .await()

                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Xác thực email thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            } else {
                                isLoading = false
                                error = "Mã OTP hoặc Email không chính xác"
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            error = e.message ?: "Quá trình xác thực gặp lỗi"
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

        // ERROR MESSAGE DISPLAY (Đặt ngoài cùng để bắt trọn mọi loại lỗi)
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
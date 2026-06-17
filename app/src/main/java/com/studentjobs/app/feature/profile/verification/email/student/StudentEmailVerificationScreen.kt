package com.studentjobs.app.feature.profile.verification.email.student

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val functions = remember { FirebaseFunctions.getInstance("us-east1") }
    val schoolDomainService = remember { SchoolDomainService() }
    val trustRepository = remember { TrustRepository(TrustService(), UserServiceNew()) }

    var studentEmail by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isOtpSent by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFE0F2FE), Color(0xFFFCE7F3))
    )
    val btnGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF4F46E5), Color(0xFF06B6D4))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Xác Thực Email Sinh Viên ✉️",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1B4B),
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Nhập email do trường cấp (.edu) để chứng minh bồ là sinh viên chính hiệu nha.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF4B5563)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // TEXT FIELD CUSTOM STYLE
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4F46E5),
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedLabelColor = Color(0xFF4F46E5),
                unfocusedLabelColor = Color.Gray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color(0xFF1E1B4B),
                unfocusedTextColor = Color(0xFF1E1B4B)
            )

            OutlinedTextField(
                value = studentEmail,
                onValueChange = { studentEmail = it },
                label = { Text("Email Sinh viên (ví dụ: nguyenvanb@hcmut.edu.vn)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = textFieldColors,
                enabled = !isOtpSent && !isLoading
            )

            if (isOtpSent) {
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("Nhập 6 số mã OTP gửi về Mail") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // GRADIENT BUTTON
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
                                    error =
                                        "Email không thuộc danh sách tên miền trường học liên kết"
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
                            try {
                                isLoading = true
                                val otpDoc =
                                    firestore.collection("email_otps").document(user.uid).get()
                                        .await()
                                val savedOtp = otpDoc.getString("otp")
                                val savedEmail = otpDoc.getString("email")

                                if (savedOtp == otp && savedEmail == studentEmail) {
                                    val verificationBeforeUpdate =
                                        firestore.collection("student_verifications")
                                            .document(user.uid).get().await()
                                            .toObject(StudentVerification::class.java)

                                    val wasEmailVerified =
                                        verificationBeforeUpdate?.studentEmailVerified == VerificationStatus.VERIFIED

                                    firestore.collection("student_verifications").document(user.uid)
                                        .update(mapOf("studentEmailVerified" to VerificationStatus.VERIFIED))
                                        .await()

                                    firestore.collection("students").document(user.uid)
                                        .update(mapOf("studentEmail" to studentEmail)).await()

                                    val studentVerification =
                                        firestore.collection("student_verifications")
                                            .document(user.uid).get().await()
                                            .toObject(StudentVerification::class.java)

                                    val isAllVerified =
                                        studentVerification?.studentEmailVerified == VerificationStatus.VERIFIED
                                                && studentVerification.studentPhoneVerified == VerificationStatus.VERIFIED
                                                && studentVerification.studentCardVerified == VerificationStatus.VERIFIED

                                    firestore.collection("users").document(user.uid)
                                        .update(mapOf("userVerified" to isAllVerified)).await()

                                    if (!wasEmailVerified) {
                                        trustRepository.addTrustEvent(
                                            uid = user.uid,
                                            actionType = "EMAIL_VERIFIED",
                                            changeAmount = 10,
                                            description = "Xác thực thành công email sinh viên"
                                        )
                                    }

                                    firestore.collection("email_otps").document(user.uid).delete()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .then(
                        if (!isLoading) Modifier.background(
                            btnGradient,
                            RoundedCornerShape(16.dp)
                        ) else Modifier
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(
                        text = if (!isOtpSent) "Gửi mã OTP về Mail 🚀" else "Xác thực mã OTP ngay ✨",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }

            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
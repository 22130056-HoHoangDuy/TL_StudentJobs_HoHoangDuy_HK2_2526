package com.studentjobs.app.feature.profile.verification.phone.student

import android.app.Activity
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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.data.repository.trust.TrustRepository
import com.studentjobs.app.firebase.firestore.TrustService
import com.studentjobs.app.firebase.firestore.UserServiceNew
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

@Composable
fun StudentPhoneVerificationScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val trustRepository = remember { TrustRepository(TrustService(), UserServiceNew()) }

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

    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFE0F2FE), Color(0xFFFCE7F3))
    )
    val btnGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF4F46E5), Color(0xFF06B6D4))
    )

    suspend fun updateStudentPhoneVerification(uid: String, phoneNum: String) {
        val verificationBeforeUpdate = firestore.collection("student_verifications")
            .document(uid).get().await().toObject(StudentVerification::class.java)

        val wasPhoneVerified =
            verificationBeforeUpdate?.studentPhoneVerified == VerificationStatus.VERIFIED

        firestore.collection("student_verifications").document(uid)
            .update(mapOf("studentPhoneVerified" to VerificationStatus.VERIFIED)).await()

        firestore.collection("users").document(uid).update(mapOf("phoneNumber" to phoneNum)).await()

        val studentVerification = firestore.collection("student_verifications")
            .document(uid).get().await().toObject(StudentVerification::class.java)

        val isAllVerified = studentVerification?.studentEmailVerified == VerificationStatus.VERIFIED
                && studentVerification.studentPhoneVerified == VerificationStatus.VERIFIED
                && studentVerification.studentCardVerified == VerificationStatus.VERIFIED

        firestore.collection("users").document(uid).update(mapOf("userVerified" to isAllVerified))
            .await()

        if (!wasPhoneVerified) {
            trustRepository.addTrustEvent(
                uid = uid,
                actionType = "PHONE_VERIFIED",
                changeAmount = 10,
                description = "Xác thực số điện thoại"
            )
        }
    }

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
                text = "Xác Thực Số Điện Thoại 📱",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1B4B),
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Để các chủ nhà hàng, quán cafe liên hệ nhận bồ đi làm siêu tốc trong ngày nha.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF4B5563)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

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
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Số điện thoại di động") },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = textFieldColors,
                enabled = !isCodeSent && !isLoading
            )

            if (isCodeSent) {
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("Mã OTP gửi qua tin nhắn SMS") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    error = ""
                    if (!isCodeSent) {
                        if (phone.length < 9) {
                            error = "Số điện thoại không hợp lệ (Tối thiểu 9 số)"
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
                                            updateStudentPhoneVerification(uid, formattedPhone)
                                            isLoading = false
                                            Toast.makeText(
                                                context,
                                                "Xác thực thành công!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack()
                                        } catch (e: Exception) {
                                            isLoading = false
                                            error = e.message ?: "Tự động xác thực thất bại"
                                        }
                                    }
                                }

                                override fun onVerificationFailed(e: FirebaseException) {
                                    isLoading = false
                                    error = e.message ?: "Gửi mã SMS thất bại"
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
                                        "Đã gửi mã OTP thành công!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }).build()
                        PhoneAuthProvider.verifyPhoneNumber(options)
                    } else {
                        if (otp.length < 6) {
                            error = "Mã OTP phải có độ dài 6 ký tự"
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
                                            updateStudentPhoneVerification(uid, formattedPhone)
                                            isLoading = false
                                            Toast.makeText(
                                                context,
                                                "Xác thực số điện thoại thành công!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack()
                                        } catch (e: Exception) {
                                            isLoading = false
                                            error = e.message ?: "Lỗi cập nhật dữ liệu xác thực"
                                        }
                                    } else {
                                        isLoading = false
                                        error = task.exception?.message ?: "Mã OTP không chính xác"
                                    }
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
                        text = if (!isCodeSent) "Gửi mã SMS xác thực 🚀" else "Xác nhận mã OTP ✨",
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
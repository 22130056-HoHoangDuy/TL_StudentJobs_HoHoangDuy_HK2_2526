package com.studentjobs.app.feature.profile.verification.phone.student

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

    // Firebase Instantiation
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val trustRepository = remember {
        TrustRepository(
            TrustService(),
            UserServiceNew()
        )
    }

    // States
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // Helper: Định dạng số điện thoại chuẩn quốc tế (+84)
    val formattedPhone = remember(phone) {
        when {
            phone.startsWith("0") -> "+84${phone.drop(1)}"
            phone.startsWith("+84") -> phone
            else -> "+84$phone"
        }
    }

    // Hàm suspend xử lý nghiệp vụ cập nhật Database tập trung
    suspend fun updateStudentPhoneVerification(
        uid: String,
        phoneNum: String
    ) {

        val verificationBeforeUpdate =

            firestore.collection("student_verifications")
                .document(uid)
                .get()
                .await()
                .toObject(StudentVerification::class.java)

        val wasPhoneVerified =

            verificationBeforeUpdate?.studentPhoneVerified ==
                    VerificationStatus.VERIFIED

        // update phone verify

        firestore.collection("student_verifications")
            .document(uid)
            .update(
                mapOf(
                    "studentPhoneVerified"
                            to VerificationStatus.VERIFIED
                )
            )
            .await()

        // update phone

        firestore.collection("users")
            .document(uid)
            .update(
                mapOf(
                    "phoneNumber" to phoneNum
                )
            )
            .await()

        val studentVerification =

            firestore.collection("student_verifications")
                .document(uid)
                .get()
                .await()
                .toObject(StudentVerification::class.java)

        val isAllVerified =

            studentVerification?.studentEmailVerified ==
                    VerificationStatus.VERIFIED

                    &&

                    studentVerification.studentPhoneVerified ==
                    VerificationStatus.VERIFIED

                    &&

                    studentVerification.studentCardVerified ==
                    VerificationStatus.VERIFIED

        firestore.collection("users")
            .document(uid)
            .update(
                mapOf(
                    "userVerified" to isAllVerified
                )
            )
            .await()

        // TRUST SCORE

        if (!wasPhoneVerified) {

            trustRepository.addTrustEvent(

                uid = uid,

                actionType =
                    "PHONE_VERIFIED",

                changeAmount = 10,

                description =
                    "Xác thực số điện thoại"
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Xác thực Số điện thoại",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // INPUT PHONE FIELD
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isCodeSent && !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // INPUT OTP FIELD
        if (isCodeSent) {
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Mã OTP xác thực") },
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
                    // LUỒNG 1: GỬI MÃ OTP QUA SMS
                    // ========================================
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
                                // Tự động xác thực nếu Firebase nhận diện được SMS không cần điền mã
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
                    // ========================================
                    // LUỒNG 2: XÁC THỰC MÃ OTP DO USER NHẬP
                    // ========================================
                    if (otp.length < 6) {
                        error = "Mã OTP phải có độ dài 6 ký tự"
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

        // ERROR MESSAGE DISPLAY (Luôn hiện thị đúng vị trí khi có lỗi)
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
package com.studentjobs.app.feature.profile.verification.email.student

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.firebase.firestore.SchoolDomainService
import com.studentjobs.app.utils.calculateTrustScore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun StudentEmailVerificationScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val auth = FirebaseAuth.getInstance()

    val firestore = FirebaseFirestore.getInstance()

    val functions = FirebaseFunctions.getInstance("us-east1")

    val scope = rememberCoroutineScope()

    val schoolDomainService = remember {
        SchoolDomainService()
    }

    var studentEmail by remember {
        mutableStateOf("")
    }

    var otp by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var isOtpSent by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Student Email Verification", style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // EMAIL
        OutlinedTextField(
            value = studentEmail, onValueChange = {
            studentEmail = it
        }, label = {
            Text("Student Email")
        }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // OTP FIELD
        if (isOtpSent) {

            OutlinedTextField(
                value = otp, onValueChange = {
                otp = it
            }, label = {
                Text("OTP Code")
            }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // SEND OTP
        if (!isOtpSent) {

            Button(
                onClick = {

                    scope.launch {

                        try {

                            error = ""

                            if (!studentEmail.contains("@")) {

                                error = "Invalid email"

                                return@launch
                            }

                            val domain = studentEmail.substringAfter("@")

                            isLoading = true

                            val isValid = schoolDomainService.isValidStudentDomain(domain)

                            if (!isValid) {

                                isLoading = false

                                error = "Invalid student domain"

                                return@launch
                            }

                            val user = auth.currentUser

                            if (user == null) {

                                isLoading = false

                                error = "User not found"

                                return@launch
                            }

                            // CALL CLOUD FUNCTION
                            val data = hashMapOf(
                                "email" to studentEmail, "uid" to user.uid
                            )

                            functions.getHttpsCallable("sendVerificationOtp").call(data).await()

                            isLoading = false

                            isOtpSent = true

                            Toast.makeText(
                                context, "OTP sent to email", Toast.LENGTH_SHORT
                            ).show()

                        } catch (e: Exception) {

                            isLoading = false

                            error = e.message ?: "Failed to send OTP"
                        }
                    }
                }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading
            ) {

                if (isLoading) {

                    CircularProgressIndicator()

                } else {

                    Text("Send OTP")
                }
            }

        } else {

            // ========================================
// VERIFY OTP
// ========================================

            Button(
                onClick = {

                    scope.launch {

                        try {

                            isLoading = true

                            val user = auth.currentUser

                            if (user == null) {

                                isLoading = false

                                error = "User not found"

                                return@launch
                            }

                            // ========================================
                            // GET OTP
                            // ========================================

                            val otpDoc =
                                firestore.collection("email_otps").document(user.uid).get().await()

                            val savedOtp = otpDoc.getString("otp")

                            val savedEmail = otpDoc.getString("email")

                            if (

                                savedOtp == otp &&

                                savedEmail == studentEmail

                            ) {

                                // ========================================
                                // UPDATE STUDENT VERIFICATION
                                // ========================================

                                firestore.collection("student_verifications").document(user.uid)
                                    .update(
                                        mapOf(

                                            "studentEmailVerified" to VerificationStatus.VERIFIED

                                        )
                                    ).await()

                                // ========================================
                                // UPDATE STUDENT PROFILE
                                // ========================================

                                firestore.collection("students").document(user.uid).update(
                                        mapOf(
                                            "studentEmail" to studentEmail
                                        )
                                    ).await()

                                // ========================================
                                // LOAD FULL STATE
                                // ========================================

                                val userCore =

                                    firestore.collection("users").document(user.uid).get().await()
                                        .toObject(
                                            UserCore::class.java
                                        )

                                val studentVerification =

                                    firestore.collection("student_verifications").document(user.uid)
                                        .get().await().toObject(
                                            StudentVerification::class.java
                                        )

                                // ========================================
                                // RECALCULATE TRUST SCORE
                                // ========================================

                                val trustScore =

                                    calculateTrustScore(

                                        user = userCore!!,

                                        studentVerification = studentVerification
                                    )

                                // ========================================
                                // CHECK FINAL VERIFIED
                                // ========================================

                                val userVerified =

                                    studentVerification?.studentEmailVerified ==

                                            VerificationStatus.VERIFIED

                                            &&

                                            studentVerification.studentPhoneVerified ==

                                            VerificationStatus.VERIFIED

                                            &&

                                            studentVerification.studentCardVerified ==

                                            VerificationStatus.VERIFIED

                                // ========================================
                                // UPDATE USER CORE
                                // ========================================

                                firestore.collection("users").document(user.uid).update(
                                        mapOf(

                                            "trustScore" to trustScore,

                                            "userVerified" to userVerified
                                        )
                                    ).await()

                                isLoading = false

                                Toast.makeText(
                                    context, "Student email verified", Toast.LENGTH_SHORT
                                ).show()

                                navController.popBackStack()

                            } else {

                                isLoading = false

                                error = "Invalid OTP"
                            }

                        } catch (e: Exception) {

                            isLoading = false

                            error = e.message ?: "Verification failed"
                        }
                    }
                }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading
            ) {

                if (isLoading) {

                    CircularProgressIndicator()

                } else {

                    Text("Verify OTP")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (error.isNotEmpty()) {

                Text(
                    text = error, color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


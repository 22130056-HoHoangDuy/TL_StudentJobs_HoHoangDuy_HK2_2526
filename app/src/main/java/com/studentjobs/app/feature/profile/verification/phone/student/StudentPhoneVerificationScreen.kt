package com.studentjobs.app.feature.profile.verification.phone.student

import android.app.Activity
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
import com.studentjobs.app.data.model.user.UserCore
import com.studentjobs.app.utils.calculateTrustScore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@Composable
fun StudentPhoneVerificationScreen(
    navController: NavController
) {

    val context = LocalContext.current

    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    var verificationId by remember { mutableStateOf("") }

    var isCodeSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    suspend fun updateStudentPhoneVerification(
        uid: String, formattedPhone: String
    ) {

        // =============================================
        // UPDATE PHONE VERIFIED
        // =============================================

        firestore.collection("student_verifications").document(uid).update(
            mapOf(
                "studentPhoneVerified" to VerificationStatus.VERIFIED
            )
        ).await()

        // =============================================
        // LOAD LATEST USER
        // =============================================

        val user =
            firestore.collection("users").document(uid).get().await().toObject(UserCore::class.java)

        // =============================================
        // LOAD LATEST VERIFICATION
        // =============================================

        val studentVerification =
            firestore.collection("student_verifications").document(uid).get().await()
                .toObject(StudentVerification::class.java)

        // =============================================
        // RECALCULATE TRUST SCORE
        // =============================================

        val trustScore = calculateTrustScore(
            user = user!!, studentVerification = studentVerification
        )

        // =============================================
        // CHECK FINAL VERIFIED
        // =============================================

        val userVerified =

            studentVerification?.studentEmailVerified == VerificationStatus.VERIFIED

                    &&

                    studentVerification.studentPhoneVerified == VerificationStatus.VERIFIED

                    &&

                    studentVerification.studentCardVerified == VerificationStatus.VERIFIED

        // =============================================
        // UPDATE USER CORE
        // =============================================

        firestore.collection("users").document(uid).update(
            mapOf(

                "phoneNumber" to formattedPhone,

                "trustScore" to trustScore,

                "userVerified" to userVerified
            )
        ).await()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Phone Verification", style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = phone, onValueChange = {
                phone = it
            }, label = {
                Text("Phone number")
            }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (!isCodeSent) {

            Button(
                onClick = {

                    if (phone.length < 9) {

                        Toast.makeText(
                            context, "Invalid phone number", Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    val formattedPhone = when {

                        phone.startsWith("0") -> {
                            "+84${phone.drop(1)}"
                        }

                        phone.startsWith("+84") -> {
                            phone
                        }

                        else -> {
                            "+84$phone"
                        }
                    }

                    isLoading = true

                    val options =
                        PhoneAuthOptions.newBuilder(auth).setPhoneNumber(formattedPhone).setTimeout(
                            60L, TimeUnit.SECONDS
                        ).setActivity(
                            navController.context as Activity
                        ).setCallbacks(

                            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                override fun onVerificationCompleted(
                                    credential: PhoneAuthCredential
                                ) {

                                    auth.currentUser?.updatePhoneNumber(
                                        credential
                                    )?.addOnCompleteListener {

                                        val uid = auth.currentUser?.uid
                                            ?: return@addOnCompleteListener

                                        CoroutineScope(
                                            Dispatchers.IO
                                        ).launch {

                                            try {

                                                updateStudentPhoneVerification(
                                                    uid, formattedPhone
                                                )

                                                withContext(
                                                    Dispatchers.Main
                                                ) {

                                                    isLoading = false

                                                    Toast.makeText(
                                                        context,
                                                        "Phone verified",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    navController.popBackStack()
                                                }

                                            } catch (e: Exception) {

                                                withContext(
                                                    Dispatchers.Main
                                                ) {

                                                    isLoading = false

                                                    Toast.makeText(
                                                        context,
                                                        e.message,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                        }
                                    }
                                }

                                override fun onVerificationFailed(
                                    e: FirebaseException
                                ) {

                                    isLoading = false

                                    Toast.makeText(
                                        context, e.message, Toast.LENGTH_LONG
                                    ).show()

                                    e.printStackTrace()
                                }

                                override fun onCodeSent(
                                    id: String, token: PhoneAuthProvider.ForceResendingToken
                                ) {

                                    verificationId = id
                                    isCodeSent = true
                                    isLoading = false

                                    Toast.makeText(
                                        context, "OTP sent", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }).build()

                    PhoneAuthProvider.verifyPhoneNumber(options)

                }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading
            ) {

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Send OTP")
                }
            }

        } else {

            OutlinedTextField(
                value = otp, onValueChange = {
                    otp = it
                }, label = {
                    Text("OTP Code")
                }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {

                    if (otp.length < 6) {

                        Toast.makeText(
                            context, "Invalid OTP", Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    isLoading = true

                    val credential = PhoneAuthProvider.getCredential(
                        verificationId, otp
                    )

                    auth.currentUser?.updatePhoneNumber(credential)?.addOnCompleteListener { task ->

                        val success =
                            task.isSuccessful || task.exception?.message?.contains("already been linked") == true

                        if (success) {

                            val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                            val formattedPhone = when {

                                phone.startsWith("0") -> {
                                    "+84${phone.drop(1)}"
                                }

                                phone.startsWith("+84") -> {
                                    phone
                                }

                                else -> {
                                    "+84$phone"
                                }
                            }

                            CoroutineScope(
                                Dispatchers.IO
                            ).launch {

                                try {

                                    updateStudentPhoneVerification(
                                        uid, formattedPhone
                                    )

                                    withContext(
                                        Dispatchers.Main
                                    ) {

                                        isLoading = false

                                        Toast.makeText(
                                            context, "Phone verified", Toast.LENGTH_SHORT
                                        ).show()

                                        navController.popBackStack()
                                    }

                                } catch (e: Exception) {

                                    withContext(
                                        Dispatchers.Main
                                    ) {

                                        isLoading = false

                                        Toast.makeText(
                                            context, e.message, Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }

                        } else {

                            isLoading = false

                            Toast.makeText(
                                context, task.exception?.message, Toast.LENGTH_LONG
                            ).show()
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
        }
    }
}
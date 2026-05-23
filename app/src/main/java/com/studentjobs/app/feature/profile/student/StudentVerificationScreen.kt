package com.studentjobs.app.feature.profile.student

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.firebase.firestore.VerificationService
import kotlinx.coroutines.launch

@Composable
fun StudentVerificationScreen(

    navController: NavController

) {

    val verificationService = remember {

        VerificationService()
    }

    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var verification by remember {

        mutableStateOf<StudentVerification?>(null)
    }

    var frontImage by remember {

        mutableStateOf<Uri?>(null)
    }

    var backImage by remember {

        mutableStateOf<Uri?>(null)
    }

    var isUploading by remember {

        mutableStateOf(false)
    }

    // ====================================
    // LOAD VERIFICATION
    // ====================================

    LaunchedEffect(uid) {

        if (uid != null) {

            verificationService.listenStudentVerification(uid) {

                    verification = it
                }
        }
    }

    // ====================================
    // AUTO CLOSE
    // ====================================

    LaunchedEffect(
        verification?.studentCardVerified
    ) {

        if (

            verification?.studentCardVerified ==

            VerificationStatus.VERIFIED

        ) {

            navController.popBackStack()
        }
    }

    // ====================================
    // VERIFIED STATE
    // ====================================

    val isEnabled =

        verification?.studentCardVerified !=

                VerificationStatus.VERIFIED

    // ====================================
    // IMAGE PICKER
    // ====================================

    val frontPicker =

        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            frontImage = uri
        }

    val backPicker =

        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            backImage = uri
        }

    val scope = rememberCoroutineScope()

    // ====================================
    // UI
    // ====================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {

        Text(
            text = "Student Verification",

            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            Modifier.height(16.dp)
        )

        // ====================================
        // FRONT IMAGE
        // ====================================

        Button(

            onClick = {

                frontPicker.launch("image/*")
            },

            enabled = isEnabled,

            modifier = Modifier.fillMaxWidth()

        ) {

            Text(

                if (frontImage == null)

                    "Choose Front Card Image"
                else

                    "Front Image Selected"
            )
        }

        Spacer(
            Modifier.height(12.dp)
        )

        // ====================================
        // BACK IMAGE
        // ====================================

        Button(

            onClick = {

                backPicker.launch("image/*")
            },

            enabled = isEnabled,

            modifier = Modifier.fillMaxWidth()

        ) {

            Text(

                if (backImage == null)

                    "Choose Back Card Image"
                else

                    "Back Image Selected"
            )
        }

        Spacer(
            Modifier.height(20.dp)
        )

        // ====================================
        // UPLOAD BUTTON
        // ====================================

        Button(

            onClick = {

                scope.launch {

                    if (uid == null) return@launch

                    if (frontImage == null || backImage == null) {
                        return@launch
                    }

                    val safeUid = uid

                    isUploading = true

                    try {

                        // ====================================
                        // UPLOAD FRONT
                        // ====================================

                        val frontUpload =

                            verificationService.uploadStudentCardImage(

                                    uid = safeUid,

                                    imageUri = frontImage!!,

                                    isFront = true
                                )

                        // ====================================
                        // UPLOAD BACK
                        // ====================================

                        val backUpload =

                            verificationService.uploadStudentCardImage(

                                    uid = safeUid,

                                    imageUri = backImage!!,

                                    isFront = false
                                )

                        if (

                            frontUpload.isFailure ||

                            backUpload.isFailure

                        ) {

                            isUploading = false

                            return@launch
                        }

                        val frontUrl = frontUpload.getOrNull()

                        val backUrl = backUpload.getOrNull()

                        // ====================================
                        // UPDATE FIRESTORE
                        // ====================================

                        verificationService.updateStudentVerificationFields(

                                uid = safeUid,

                                fields = mapOf(

                                    "studentCardFrontUrl" to frontUrl!!,

                                    "studentCardBackUrl" to backUrl!!,

                                    "studentCardVerified" to VerificationStatus.PENDING.name,

                                    "updatedAt" to System.currentTimeMillis()
                                )
                            )

                    } catch (e: Exception) {

                        e.printStackTrace()

                    } finally {

                        isUploading = false
                    }
                }
            },

            modifier = Modifier.fillMaxWidth(),

            enabled =

                frontImage != null &&

                        backImage != null &&

                        !isUploading &&

                        isEnabled
        ) {

            if (isUploading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),

                    strokeWidth = 2.dp
                )

            } else {

                Text(
                    "Upload & Verify"
                )
            }
        }

        Spacer(
            Modifier.height(16.dp)
        )

        // ====================================
        // RESULT
        // ====================================

        verification?.let {

            Card(

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(16.dp)

            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(

                        "Verification Result",

                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    Text(
                        "Status: ${
                            it.studentCardVerified
                        }"
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    when (it.studentCardVerified) {

                        VerificationStatus.VERIFIED -> {

                            Text(
                                "Verified",

                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        VerificationStatus.PENDING -> {

                            Text(
                                "Pending OCR Processing..."
                            )
                        }

                        VerificationStatus.REJECTED -> {

                            Text(
                                "Verification Rejected"
                            )
                        }

                        else -> {

                            Text(
                                "Not Verified"
                            )
                        }
                    }
                }
            }
        }
    }
}
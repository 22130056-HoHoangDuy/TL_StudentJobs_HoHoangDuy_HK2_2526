package com.studentjobs.app.feature.profile.verification.student

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
import com.studentjobs.app.feature.profile.shared.components.UploadCard
import com.studentjobs.app.firebase.firestore.UserService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StudentVerificationScreen(
    navController: NavController
) {
    val userService = remember { UserService() }
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var user by remember { mutableStateOf<com.studentjobs.app.data.model.User?>(null) }

    var frontImage by remember { mutableStateOf<Uri?>(null) }
    var backImage by remember { mutableStateOf<Uri?>(null) }

    var isUploading by remember { mutableStateOf(false) }

    // 📡 realtime user listener
    LaunchedEffect(uid) {
        if (uid != null) {
            userService.listenUser(uid) {
                user = it
            }
        }
    }

    // 🔥 auto back khi verify xong
    LaunchedEffect(user?.isStudentVerified) {
        if (user?.isStudentVerified == true) {
            delay(800)
            navController.popBackStack()
        }
    }

    // 📷 picker
    val frontPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        frontImage = uri
    }

    val backPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        backImage = uri
    }

    // 🎨 UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Student Verification",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        // 🧾 FRONT
        UploadCard(
            title = "Student Card - Front",
            imageUri = frontImage,
            onClick = { frontPicker.launch("image/*") }
        )

        // 🧾 BACK
        UploadCard(
            title = "Student Card - Back",
            imageUri = backImage,
            onClick = { backPicker.launch("image/*") }
        )

        Spacer(Modifier.height(16.dp))

        val scope = rememberCoroutineScope() // 👈 đặt ở đầu Composable

        Button(
            onClick = {
                scope.launch {
                    if (uid == null) return@launch

                    isUploading = true

                    val result = userService.uploadStudentCard(
                        uid = uid,
                        frontUri = frontImage!!,
                        backUri = backImage!!
                    )

                    isUploading = false

                    if (result.isFailure) {
                        // TODO: show error
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = frontImage != null && backImage != null && !isUploading
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Upload & Verify")
            }
        }
        Spacer(Modifier.height(16.dp))

        // 📄 RESULT
        user?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Extracted Info", style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(8.dp))

                    Text("Name: ${it.extractedName ?: "..."}")
                    Text("School: ${it.school ?: "..."}")
                    Text("DOB: ${it.dateOfBirth ?: "..."}")
                    Text("Student ID: ${it.studentId ?: "..."}")

                    Spacer(Modifier.height(8.dp))

                    if (it.isStudentVerified) {
                        Text("✅ Verified", color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text("⏳ Processing OCR...")
                    }
                }
            }
        }
    }
}
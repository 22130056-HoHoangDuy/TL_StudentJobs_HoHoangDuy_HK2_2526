package com.studentjobs.app.feature.profile.student

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.student.StudentVerification
import com.studentjobs.app.firebase.firestore.VerificationService
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun StudentVerificationScreen(
    navController: NavController
) {
    val verificationService = remember { VerificationService() }
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var verification by remember { mutableStateOf<StudentVerification?>(null) }
    var frontImage by remember { mutableStateOf<Uri?>(null) }
    var backImage by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    LaunchedEffect(uid) {
        if (uid != null) {
            verificationService.listenStudentVerification(uid) {
                verification = it
            }
        }
    }

    LaunchedEffect(verification?.studentCardVerified) {
        if (verification?.studentCardVerified == VerificationStatus.VERIFIED) {
            navController.popBackStack()
        }
    }

    val isEnabled = verification?.studentCardVerified != VerificationStatus.VERIFIED

    val frontPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> frontImage = uri }

    val backPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> backImage = uri }

    val scope = rememberCoroutineScope()

    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFE0F2FE), Color(0xFFFCE7F3))
    )
    val uploadBtnGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Xác Thực Thẻ Sinh Viên 🎓",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1B4B),
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Tải ảnh 2 mặt của thẻ sinh viên để hệ thống đối soát tài khoản chính chủ.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF4B5563)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // FRONT IMAGE PICKER
            Button(
                onClick = { frontPicker.launch("image/*") },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(2.dp, RoundedCornerShape(14.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (frontImage == null) Color.White else Color(0xFFE0F2FE),
                    contentColor = if (frontImage == null) Color(0xFF4B5563) else Color(0xFF0284C7)
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Text(
                    text = if (frontImage == null) "📸 Tải lên ảnh mặt trước thẻ" else "✅ Đã chọn ảnh mặt trước",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            // BACK IMAGE PICKER
            Button(
                onClick = { backPicker.launch("image/*") },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(2.dp, RoundedCornerShape(14.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (backImage == null) Color.White else Color(0xFFE0F2FE),
                    contentColor = if (backImage == null) Color(0xFF4B5563) else Color(0xFF0284C7)
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Text(
                    text = if (backImage == null) "📸 Tải lên ảnh mặt sau thẻ" else "✅ Đã chọn ảnh mặt sau",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(36.dp))

            // UPLOAD ACTION BUTTON
            Button(
                onClick = {
                    scope.launch {

                        val safeUid = uid ?: return@launch

                        if (
                            frontImage == null ||
                            backImage == null
                        ) {
                            return@launch
                        }

                        isUploading = true

                        try {
                            val frontUpload = verificationService.uploadStudentCardImage(
                                uid = safeUid,
                                imageUri = frontImage!!,
                                isFront = true
                            )
                            val backUpload = verificationService.uploadStudentCardImage(
                                uid = safeUid,
                                imageUri = backImage!!,
                                isFront = false
                            )

                            if (frontUpload.isFailure || backUpload.isFailure) {
                                isUploading = false
                                return@launch
                            }

                            val frontUrl = frontUpload.getOrNull()
                            val backUrl = backUpload.getOrNull()

                            verificationService.updateStudentVerificationFields(
                                uid = safeUid,
                                fields = mapOf(
                                    "studentCardFrontUrl" to frontUrl!!,
                                    "studentCardBackUrl" to backUrl!!,
                                    "studentCardVerified" to VerificationStatus.PENDING.name,
                                    "updatedAt" to Date()
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            isUploading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .then(
                        if (frontImage != null && backImage != null && !isUploading && isEnabled) Modifier.background(
                            uploadBtnGradient,
                            RoundedCornerShape(16.dp)
                        ) else Modifier
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = frontImage != null && backImage != null && !isUploading && isEnabled
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(
                        "Gửi ảnh để phê duyệt ngay 🚀",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }

            // ====================================
            // COMPLETE THE MISSING RESULT VIEW
            // ====================================
            verification?.let { status ->
                Spacer(Modifier.height(32.dp))

                val statusColor = when (status.studentCardVerified) {
                    VerificationStatus.VERIFIED -> Color(0xFF10B981)
                    VerificationStatus.PENDING -> Color(0xFFF59E0B)
                    else -> Color(0xFFEF4444)
                }
                val statusText = when (status.studentCardVerified) {
                    VerificationStatus.VERIFIED -> "ĐÃ PHÊ DUYỆT 🎉"
                    VerificationStatus.PENDING -> "ĐANG CHỜ DUYỆT ⏳"
                    else -> "CHƯA XÁC THỰC ❌"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Trạng Thái Hồ Sơ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1B4B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    statusColor.copy(alpha = 0.15f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = statusText,
                                color = statusColor,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.studentjobs.app.feature.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.feature.profile.employer.EmployerVerificationScreen
import com.studentjobs.app.feature.profile.employer.VerifiedEmployerProfile
import com.studentjobs.app.feature.profile.student.components.ProfileCompletionSection
import com.studentjobs.app.feature.profile.student.components.VerifiedStudentProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    var showPlusDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showSettingsMenu by remember { mutableStateOf(false) }

    // --- Xử lý chọn ảnh đại diện ---
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            // Thay vì gọi updateEmployerAvatar (upload lên mạng),
            // hãy gọi updateLocalAvatar (lưu vào RAM)
            viewModel.updateLocalAvatar(it.toString())
        }
    }

    LaunchedEffect(Unit) {
        savedStateHandle?.get<Double>("selected_lat")?.let { lat ->
            savedStateHandle.get<Double>("selected_lng")?.let { lng ->
                viewModel.updateStudentLocation(lat, lng)
                savedStateHandle.remove<Double>("selected_lat")
                savedStateHandle.remove<Double>("selected_lng")
            }
        }
    }

    if (state.isLoading) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A)), Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF06B6D4))
        }
        return
    }

    val isStudentVerified = state.studentVerification?.let {
        it.studentCardVerified == VerificationStatus.VERIFIED &&
                it.studentPhoneVerified == VerificationStatus.VERIFIED &&
                it.studentEmailVerified == VerificationStatus.VERIFIED
    } ?: false

    val isEmployerVerified =
        state.employerVerification?.submissionStatus == VerificationStatus.VERIFIED

    Scaffold(
        containerColor = if ((state.role == UserRole.STUDENT && !isStudentVerified) ||
            (state.role == UserRole.EMPLOYER && !isEmployerVerified)
        ) Color.White else Color(0xFF0F172A)
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Nút Settings & Menu
            IconButton(
                onClick = { showSettingsMenu = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.Settings, "Cài đặt", tint = Color.White)
            }

//            DropdownMenu(
//                expanded = showSettingsMenu,
//                onDismissRequest = { showSettingsMenu = false }) {
//                DropdownMenuItem(
//                    text = { Text("Cài đặt hệ thống") },
//                    onClick = {
//                        navController.navigate("settings_screen"); showSettingsMenu = false
//                    },
//                    leadingIcon = { Icon(Icons.Default.Settings, null) }
//                )
//                DropdownMenuItem(
//                    text = { Text("Đăng xuất", color = Color(0xFFF87171)) },
//                    onClick = { showLogoutDialog = true; showSettingsMenu = false },
//                    leadingIcon = { Icon(Icons.Default.Logout, null, tint = Color(0xFFF87171)) }
//                )
//            }

            // Nội dung chính
            Column(Modifier.fillMaxSize()) {
                when (state.role) {
                    UserRole.STUDENT -> {
                        if (isStudentVerified) {
                            VerifiedStudentProfile(
                                state,
                                {},
                                {},
                                {},
                                {},
                                { showLogoutDialog = true })
                        } else {
                            ProfileCompletionSection(
                                isStudentVerified = state.studentVerification?.studentCardVerified == VerificationStatus.VERIFIED,
                                isPhoneVerified = state.studentVerification?.studentPhoneVerified == VerificationStatus.VERIFIED,
                                isEmailVerified = state.studentVerification?.studentEmailVerified == VerificationStatus.VERIFIED,
                                // 🔥 Thêm dòng này vào:
                                isStudentEmailVerified = state.studentVerification?.studentEmailVerified == VerificationStatus.VERIFIED,

                                onStudentClick = { navController.navigate("student_verification") },
                                onPhoneClick = { navController.navigate("phone_verification/STUDENT") },
                                onEmailClick = { navController.navigate("email_verification/STUDENT") }
                            )
                        }
                    }

                    UserRole.EMPLOYER -> {
                        if (isEmployerVerified) {
                            VerifiedEmployerProfile(
                                state = state,
                                onEditSection = { section ->
                                    if (section == "header") {
                                        photoPicker.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    }
                                },
                                onUpgradePlusClick = { navController.navigate("subscription/EMPLOYER") },
                                onLogoutClick = { showLogoutDialog = true },
                                onSettingsClick = { navController.navigate("settings_screen") }
                            )
                        } else {
                            EmployerVerificationScreen(navController)
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (showLogoutDialog) {
//        AlertDialog(
//            onDismissRequest = { showLogoutDialog = false },
//            title = { Text("Đăng xuất") },
//            text = { Text("Bạn có chắc chắn muốn đăng xuất?") },
//            confirmButton = {
//                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
//                    Text(
//                        "Đăng xuất",
//                        color = Color(0xFFF87171)
//                    )
//                }
//            },
//            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Hủy") } }
//        )
    }
}
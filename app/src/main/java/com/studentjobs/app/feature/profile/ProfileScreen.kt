package com.studentjobs.app.feature.profile

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.feature.profile.employer.EmployerVerificationScreen
import com.studentjobs.app.feature.profile.employer.components.VerifiedEmployerProfile
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
    val context = LocalContext.current
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    var showPlusDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showSettingsMenu by remember { mutableStateOf(false) }

    // ========================================
    // XỬ LÝ LƯU VỊ TRÍ TỪ MAP PICKER
    // ========================================
    LaunchedEffect(Unit) {
        val lat = savedStateHandle?.get<Double>("selected_lat")
        val lng = savedStateHandle?.get<Double>("selected_lng")

        if (lat != null && lng != null) {
            viewModel.updateStudentLocation(latitude = lat, longitude = lng)
            savedStateHandle.remove<Double>("selected_lat")
            savedStateHandle.remove<Double>("selected_lng")
        }
    }

    // ========================================
    // XỬ LÝ LƯU SKILLS
    // ========================================
    LaunchedEffect(Unit) {
        val categories = savedStateHandle?.get<List<String>>("selected_categories")
        val skills = savedStateHandle?.get<List<String>>("selected_skills")

        if (categories != null && skills != null) {
            viewModel.updateStudentSkills(categories = categories, skills = skills)
            savedStateHandle.remove<List<String>>("selected_categories")
            savedStateHandle.remove<List<String>>("selected_skills")
        }
    }

    // ========================================
    // LOADING STATE
    // ========================================
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF06B6D4))
        }
        return
    }

    // ========================================
    // CHUẨN HÓA SMART CAST CHO CÁC BIẾN DELEGATED
    // ========================================
    val studentVerificationLocal = state.studentVerification
    val isStudentVerified = studentVerificationLocal != null
            && studentVerificationLocal.studentCardVerified == VerificationStatus.VERIFIED
            && studentVerificationLocal.studentPhoneVerified == VerificationStatus.VERIFIED
            && studentVerificationLocal.studentEmailVerified == VerificationStatus.VERIFIED

    val employerVerificationLocal = state.employerVerification
    val isEmployerVerified = employerVerificationLocal != null
            && employerVerificationLocal.submissionStatus == VerificationStatus.VERIFIED

    // Nếu chưa xác thực thì dùng nền trắng nguyên bản, ngược lại dùng nền tối đêm huyền bí
    val currentContainerColor = if (state.role == UserRole.STUDENT && !isStudentVerified) {
        Color.White
    } else if (state.role == UserRole.EMPLOYER && !isEmployerVerified) {
        Color.White
    } else {
        Color(0xFF0F172A)
    }

    Scaffold(
        containerColor = currentContainerColor
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // NÚT CÀI ĐẶT FLOATING: Tự động thích ứng màu sắc theo nền
            val iconTint =
                if (currentContainerColor == Color.White) Color(0xFF0F172A) else Color.White

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
            ) {
                IconButton(onClick = { showSettingsMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Cài đặt",
                        tint = iconTint
                    )
                }

                DropdownMenu(
                    expanded = showSettingsMenu,
                    onDismissRequest = { showSettingsMenu = false },
                    modifier = Modifier.background(
                        if (currentContainerColor == Color.White) Color.White else Color(0xFF1E293B)
                    )
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Cài đặt hệ thống",
                                color = if (currentContainerColor == Color.White) Color.Black else Color.White
                            )
                        },
                        onClick = {
                            showSettingsMenu = false
                            navController.navigate("settings_screen")
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color(0xFF38BDF8)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Đăng xuất", color = Color(0xFFF87171)) },
                        onClick = {
                            showSettingsMenu = false
                            showLogoutDialog = true
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Logout,
                                contentDescription = null,
                                tint = Color(0xFFF87171)
                            )
                        }
                    )
                }
            }

            // ========================================
            // NỘI DUNG CHÍNH CỦA PROFILE
            // ========================================
            Column(modifier = Modifier.fillMaxSize()) {
                when (state.role) {
                    UserRole.STUDENT -> {
                        if (isStudentVerified) {
                            VerifiedStudentProfile(
                                state = state,
                                onUpgradePlusClick = { navController.navigate("subscription/STUDENT") },
                                onScheduleClick = {
                                    if (state.userCore?.subscriptionPlan == SubscriptionPlan.PLUS) {
                                        navController.navigate("schedule")
                                    } else {
                                        showPlusDialog = true
                                    }
                                },
                                onSelectLocation = { navController.navigate("location_picker") },
                                onManageSkills = { navController.navigate("manage_skills") },
                                onLogoutClick = { showLogoutDialog = true }
                            )
                        } else {
                            ProfileCompletionSection(
                                isStudentVerified = studentVerificationLocal?.studentCardVerified == VerificationStatus.VERIFIED,
                                isPhoneVerified = studentVerificationLocal?.studentPhoneVerified == VerificationStatus.VERIFIED,
                                isEmailVerified = studentVerificationLocal?.studentEmailVerified == VerificationStatus.VERIFIED,
                                isStudentEmailVerified = studentVerificationLocal?.studentEmailVerified == VerificationStatus.VERIFIED,
                                onStudentClick = {
                                    if (studentVerificationLocal?.studentCardVerified != VerificationStatus.VERIFIED) {
                                        navController.navigate("student_verification") {
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                onPhoneClick = {
                                    if (studentVerificationLocal?.studentPhoneVerified != VerificationStatus.VERIFIED) {
                                        navController.navigate("phone_verification/STUDENT")
                                    }
                                },
                                onEmailClick = {
                                    if (studentVerificationLocal?.studentEmailVerified != VerificationStatus.VERIFIED) {
                                        navController.navigate("email_verification/STUDENT")
                                    }
                                }
                            )
                        }
                    }

                    UserRole.EMPLOYER -> {
                        if (isEmployerVerified) {
                            VerifiedEmployerProfile(
                                state = state,
                                onEditSection = {},
                                onUpgradePlusClick = { navController.navigate("subscription/EMPLOYER") },
                                onLogoutClick = { showLogoutDialog = true },
                                onSelectLocation = { navController.navigate("location_picker") }
                            )
                        } else {
                            EmployerVerificationScreen(navController)
                        }
                    }
                }
            }
        }
    }

    // ========================================
    // DIALOG THÔNG BÁO TÍNH NĂNG PLUS
    // ========================================
    if (showPlusDialog) {
        AlertDialog(
            onDismissRequest = { showPlusDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showPlusDialog = false; navController.navigate("subscription/STUDENT")
                }) {
                    Text(
                        text = "Nâng cấp ngay",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB300)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showPlusDialog = false }) {
                    Text(text = "Để sau", color = Color.Gray)
                }
            },
            title = { Text(text = "Đặc quyền Hội viên PLUS ⭐") },
            text = { Text(text = "Hãy nâng cấp lên tài khoản PLUS để kích hoạt công nghệ Quét lịch học thông minh (OCR) và tự động rải hồ sơ xin việc phù hợp nhé!") }
        )
    }

    // ========================================
    // DIALOG XÁC NHẬN ĐĂNG XUẤT
    // ========================================
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Đăng xuất") },
            text = { Text("Bạn có chắc chắn muốn đăng xuất khỏi ứng dụng?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Đăng xuất", color = Color(0xFFF87171), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )
    }
}
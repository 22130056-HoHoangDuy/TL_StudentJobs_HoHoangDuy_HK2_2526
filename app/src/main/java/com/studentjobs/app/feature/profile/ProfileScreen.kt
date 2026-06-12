package com.studentjobs.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.data.model.user.SubscriptionPlan
import com.studentjobs.app.data.model.user.UserRole
import com.studentjobs.app.feature.profile.employer.EmployerVerificationScreen
import com.studentjobs.app.feature.profile.employer.components.VerifiedEmployerProfile
import com.studentjobs.app.feature.profile.student.components.ProfileCompletionSection
import com.studentjobs.app.feature.profile.student.components.VerifiedStudentProfile

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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Column tổng làm bệ đỡ tĩnh hoàn toàn, KHÔNG có .verticalScroll để tránh lỗi văng app
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (state.role) {
            // ========================================
            // LUỒNG SINH VIÊN (STUDENT FLOW)
            // ========================================
            UserRole.STUDENT -> {
                val verification = state.studentVerification
                val fullyVerified = verification?.studentCardVerified == VerificationStatus.VERIFIED
                        && verification.studentPhoneVerified == VerificationStatus.VERIFIED
                        && verification.studentEmailVerified == VerificationStatus.VERIFIED

                if (fullyVerified) {
                    VerifiedStudentProfile(
                        state = state,
                        onUpgradePlusClick = {
                            navController.navigate("subscription/STUDENT")
                        },
                        onScheduleClick = {
                            if (state.userCore?.subscriptionPlan == SubscriptionPlan.PLUS) {
                                navController.navigate("schedule")
                            } else {
                                showPlusDialog = true
                            }
                        },
                        onSelectLocation = {
                            navController.navigate("location_picker")
                        },
                        onManageSkills = {
                            navController.navigate("manage_skills")
                        },
                        onLogoutClick = {
                            showLogoutDialog = true
                        }
                    )
                } else {
                    ProfileCompletionSection(
                        isStudentVerified = verification?.studentCardVerified == VerificationStatus.VERIFIED,
                        isPhoneVerified = verification?.studentPhoneVerified == VerificationStatus.VERIFIED,
                        isEmailVerified = verification?.studentEmailVerified == VerificationStatus.VERIFIED,
                        isStudentEmailVerified = verification?.studentEmailVerified == VerificationStatus.VERIFIED,
                        onStudentClick = {
                            if (verification?.studentCardVerified != VerificationStatus.VERIFIED) {
                                navController.navigate("student_verification") {
                                    launchSingleTop = true
                                }
                            }
                        },
                        onPhoneClick = {
                            if (verification?.studentPhoneVerified != VerificationStatus.VERIFIED) {
                                navController.navigate("phone_verification/STUDENT")
                            }
                        },
                        onEmailClick = {
                            if (verification?.studentEmailVerified != VerificationStatus.VERIFIED) {
                                navController.navigate("email_verification/STUDENT")
                            }
                        }
                    )
                }
            }

            // ========================================
            // LUỒNG DOANH NGHIỆP (EMPLOYER FLOW)
            // ========================================
            UserRole.EMPLOYER -> {
                val verification = state.employerVerification
                val fullyVerified = verification?.submissionStatus == VerificationStatus.VERIFIED

                if (fullyVerified) {
                    VerifiedEmployerProfile(
                        state = state,
                        onEditSection = { section ->
                            when (section) {
                                "header" -> {}
                                "info" -> {}
                                "contact" -> {}
                            }
                        },
                        onUpgradePlusClick = {
                            navController.navigate("subscription/EMPLOYER")
                        },
                        onLogoutClick = {
                            showLogoutDialog = true
                        },
                        onSelectLocation = {
                            navController.navigate("location_picker")
                        }
                    )
                } else {
                    EmployerVerificationScreen(navController)
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
                TextButton(
                    onClick = {
                        showPlusDialog = false
                        navController.navigate("subscription/STUDENT")
                    }
                ) {
                    Text(text = "Continue to PLUS")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPlusDialog = false }) {
                    Text(text = "Not now")
                }
            },
            title = { Text(text = "PLUS Feature") },
            text = { Text(text = "Upgrade to PLUS to use timetable OCR and Smart Auto Apply.") }
        )
    }

    // ========================================
    // DIALOG XÁC NHẬN ĐĂNG XUẤT
    // ========================================
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Đăng xuất") },
            text = { Text("Bạn có chắc muốn đăng xuất?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Đăng xuất")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
package com.studentjobs.app.feature.profile.employer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.feature.profile.employer.components.BusinessInfoComponent
import com.studentjobs.app.feature.profile.shared.components.VerificationBanner
import com.studentjobs.app.feature.profile.shared.components.VerificationCard

@Composable
fun EmployerVerificationScreen(
    navController: NavController,
    viewModel: EmployerVerificationViewModel = viewModel()
) {
    val state = viewModel.uiState
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // --- Logic xử lý Location ---
    val latFlow =
        remember(savedStateHandle) { savedStateHandle?.getStateFlow<Double?>("selected_lat", null) }
    val lngFlow =
        remember(savedStateHandle) { savedStateHandle?.getStateFlow<Double?>("selected_lng", null) }

    val selectedLatState by (latFlow?.collectAsState() ?: remember { mutableStateOf(null) })
    val selectedLngState by (lngFlow?.collectAsState() ?: remember { mutableStateOf(null) })

    LaunchedEffect(selectedLatState, selectedLngState) {
        if (selectedLatState != null && selectedLngState != null) {
            viewModel.setBusinessLocation(selectedLatState!!, selectedLngState!!)
            savedStateHandle?.remove<Double>("selected_lat")
            savedStateHandle?.remove<Double>("selected_lng")
        }
    }

    // --- Pickers ---
    val businessLicensePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.onBusinessLicenseUploaded(it) }
        }
    val storefrontPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { viewModel.onStorefrontUploaded(it) }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. Banner ---
        VerificationBanner(
            title = if (state.verificationSubmitted) "Đã nộp hồ sơ" else "Xác thực Doanh nghiệp",
            subtitle = if (state.verificationSubmitted) "Thông tin đang được duyệt (1-2 ngày làm việc)" else "Hoàn thành danh mục dưới đây để nâng uy tín"
        )

        // --- 2. Khối thông tin chung ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Thông tin doanh nghiệp",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                // --- 2. Khối thông tin chung ---
                // Thay thế toàn bộ Card chứa BusinessInfoInputCard bằng component mới này:
                BusinessInfoComponent(
                    isEditable = !state.verificationSubmitted, // Chỉ cho sửa khi chưa nộp hồ sơ
                    businessName = state.businessName,
                    businessCategory = state.businessCategory,
                    businessAddress = state.businessAddressText,
                    businessDesc = state.businessDescription,
                    businessUrl = state.businessLocationUrl,
                    // Gán các hàm callback từ ViewModel
                    onNameChange = viewModel::onBusinessNameChange,
                    onCategoryChange = viewModel::onBusinessCategoryChange,
                    onAddressChange = viewModel::onBusinessAddressChange,
                    onDescChange = viewModel::onBusinessDescriptionChange,
                    onUrlChange = viewModel::onGoogleMapsUrlChange
                )
            }
        }

        // --- 3. Khối chi tiết xác thực (Gom nhóm) ---
        Text(
            "Chi tiết xác thực",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(start = 4.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                VerificationCard(
                    title = "Xác thực Email",
                    description = if (state.businessEmailVerified == VerificationStatus.VERIFIED) "🎯 Đã xác thực" else "Nhận mã OTP bảo mật",
                    status = state.businessEmailVerified,
                    enabled = !state.verificationSubmitted && state.businessEmailVerified != VerificationStatus.VERIFIED,
                    onClick = { navController.navigate("email_verification/EMPLOYER") }
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF1F5F9))

                VerificationCard(
                    title = "Xác thực Số điện thoại",
                    description = if (state.businessPhoneVerified == VerificationStatus.VERIFIED) "🎯 Đã xác thực" else "Xác minh hotline",
                    status = state.businessPhoneVerified,
                    enabled = !state.verificationSubmitted && state.businessPhoneVerified != VerificationStatus.VERIFIED,
                    onClick = { navController.navigate("phone_verification/EMPLOYER") }
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF1F5F9))

                VerificationCard(
                    title = "Giấy phép kinh doanh",
                    description = if (state.businessLicenseVerified == VerificationStatus.VERIFIED) "🎯 Đã duyệt" else "Tải lên ảnh rõ nét",
                    status = if (state.businessLicenseUri != null && state.businessLicenseVerified == VerificationStatus.UNVERIFIED) VerificationStatus.PENDING else state.businessLicenseVerified,
                    enabled = !state.verificationSubmitted,
                    onClick = { businessLicensePicker.launch("image/*") }
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF1F5F9))

                VerificationCard(
                    title = "Ảnh mặt tiền / Văn phòng",
                    description = if (state.businessStoreFrontUri != null) "🎯 Đã tải lên" else "Ảnh chụp bảng hiệu",
                    status = if (state.businessStoreFrontUri != null) VerificationStatus.VERIFIED else VerificationStatus.UNVERIFIED,
                    enabled = !state.verificationSubmitted,
                    onClick = { storefrontPicker.launch("image/*") }
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF1F5F9))

                VerificationCard(
                    title = "Vị trí trên bản đồ",
                    description = if (state.businessLatitude != null && state.businessLatitude != 0.0) "📍 Đã ghim tọa độ" else "Chọn vị trí doanh nghiệp",
                    status = if (state.businessLatitude != null && state.businessLatitude != 0.0) VerificationStatus.VERIFIED else VerificationStatus.UNVERIFIED,
                    enabled = !state.verificationSubmitted,
                    onClick = { navController.navigate("location_picker") }
                )
            }
        }

        // --- 4. Submit Button ---
        Button(
            onClick = { viewModel.submitVerification() },
            enabled = !state.isLoading && !state.verificationSubmitted,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
        ) {
            if (state.isLoading) CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
            else Text(
                text = if (state.verificationSubmitted) "Hồ sơ đang chờ duyệt" else "Gửi yêu cầu xác thực",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.studentjobs.app.data.model.status.VerificationStatus
import com.studentjobs.app.feature.profile.shared.components.VerificationBanner
import com.studentjobs.app.feature.profile.shared.components.VerificationCard

@Composable
fun EmployerVerificationScreen(
    navController: NavController, viewModel: EmployerVerificationViewModel = viewModel()
) {
    val state = viewModel.uiState
    // ========================================================
// ĐOẠN CODE SỬA LỖI DELEGATE GETVALUE
// ========================================================
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

// 1. Dùng remember để lấy ra StateFlow an toàn (nếu savedStateHandle null thì trả về một StateFlow trống)
    val latFlow = remember(savedStateHandle) {
        savedStateHandle?.getStateFlow<Double?>("selected_lat", null)
    }
    val lngFlow = remember(savedStateHandle) {
        savedStateHandle?.getStateFlow<Double?>("selected_lng", null)
    }

// 2. Lúc này collectAsState() bằng 'by' sẽ mượt mà, không bao giờ lo lỗi ép kiểu hay thiếu getValue
    val selectedLatState by (latFlow?.collectAsState() ?: remember { mutableStateOf(null) })
    val selectedLngState by (lngFlow?.collectAsState() ?: remember { mutableStateOf(null) })

// 3. Giữ nguyên Effect nạp dữ liệu vào ViewModel của anh
    LaunchedEffect(selectedLatState, selectedLngState) {
        if (selectedLatState != null && selectedLngState != null) {
            viewModel.setBusinessLocation(
                latitude = selectedLatState!!, longitude = selectedLngState!!
            )
            savedStateHandle?.remove<Double>("selected_lat")
            savedStateHandle?.remove<Double>("selected_lng")
        }
    }

    // ========================================
    // LICENSE PICKER (Giấy phép kinh doanh)
    // ========================================
    val businessLicensePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onBusinessLicenseUploaded(it) }
    }

    // ========================================
    // STOREFRONT PICKER (Ảnh mặt tiền cửa hàng)
    // ========================================
    val storefrontPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onStorefrontUploaded(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)) // Nền xám nhạt dịu mắt đồng bộ toàn app
            .verticalScroll(rememberScrollState()) // Thêm scroll chống tràn màn hình khi layout dài ra
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ========================================
        // TOP BANNER (Trạng thái xét duyệt)
        // ========================================
        if (state.verificationSubmitted) {
            VerificationBanner(
                title = "Đã nộp hồ sơ xác thực",
                subtitle = "Thông tin doanh nghiệp của bạn đang được duyệt thủ công (Dự kiến 1-2 ngày làm việc)"
            )
        } else {
            VerificationBanner(
                title = "Xác thực Doanh nghiệp",
                subtitle = "Hoàn thành các danh mục dưới đây để nâng cao điểm uy tín và đăng tin tuyển dụng"
            )
        }

        // ====================================================================
// TIÊU ĐỀ NHÓM 1: Thông tin liên hệ
// ====================================================================
        Text(
            text = "Thông tin liên hệ",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )

// ========================================
// EMAIL
// ========================================
        val emailStatus = state.businessEmailVerified
        VerificationCard(
            title = "Xác thực Email Doanh nghiệp",
            description = when (emailStatus) {
                VerificationStatus.VERIFIED -> "🎯 Đã xác thực thành công"
                VerificationStatus.PENDING -> "⏳ Đang chờ hệ thống kiểm tra"
                VerificationStatus.REJECTED -> "❌ Bị từ chối, vui lòng thử lại"
                else -> "Nhận mã OTP bảo mật qua hộp thư"
            },
            status = emailStatus, // ✅ ĐÃ SỬA: Truyền trực tiếp Enum VerificationStatus
            enabled = !state.verificationSubmitted && emailStatus != VerificationStatus.VERIFIED,
            onClick = { navController.navigate("email_verification/EMPLOYER") })

// ========================================
// PHONE
// ========================================
        val phoneStatus = state.businessPhoneVerified
        VerificationCard(
            title = "Xác thực Số điện thoại",
            description = when (phoneStatus) {
                VerificationStatus.VERIFIED -> "🎯 Đã xác thực thành công"
                VerificationStatus.PENDING -> "⏳ Đang chờ kiểm tra"
                VerificationStatus.REJECTED -> "❌ Thử lại bằng số hotline khác"
                else -> "Xác minh hotline liên hệ của nhà tuyển dụng"
            },
            status = phoneStatus, // ✅ ĐÃ SỬA: Truyền trực tiếp Enum VerificationStatus
            enabled = !state.verificationSubmitted && phoneStatus != VerificationStatus.VERIFIED,
            onClick = { navController.navigate("phone_verification/EMPLOYER") })

// ====================================================================
// TIÊU ĐỀ NHÓM 2: Hồ sơ pháp lý & Vị trí
// ====================================================================
        Text(
            text = "Hồ sơ pháp lý & Vị trí",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(top = 8.dp, start = 4.dp)
        )

// ========================================
// BUSINESS LICENSE (Giấy phép kinh doanh)
// ========================================
        val currentLicenseStatus = state.businessLicenseVerified
        val hasPickedLicense = state.businessLicenseUri != null
        VerificationCard(
            title = "Giấy phép kinh doanh / MSDN",
            description = when {
                currentLicenseStatus == VerificationStatus.VERIFIED -> "🎯 Đã phê duyệt chứng nhận"
                currentLicenseStatus == VerificationStatus.PENDING -> "⏳ Giấy phép đang chờ xét duyệt"
                currentLicenseStatus == VerificationStatus.REJECTED -> "❌ Giấy phép không hợp lệ! Hãy tải lại ảnh"
                hasPickedLicense -> "✨ Đã đính kèm ảnh (Chờ bấm Gửi duyệt)"
                else -> "Tải lên ảnh chụp giấy phép rõ nét"
            },
            // ✅ ĐÃ SỬA: Nếu chọn ảnh cục bộ dưới máy thì coi như PENDING tạm thời trên UI, ngược lại lấy status thật từ server
            status = if (hasPickedLicense && currentLicenseStatus == VerificationStatus.UNVERIFIED) VerificationStatus.PENDING else currentLicenseStatus,
            enabled = !state.verificationSubmitted,
            onClick = { businessLicensePicker.launch("image/*") })

// ========================================
// STOREFRONT IMAGE (Ảnh cửa hàng/Văn phòng)
// ========================================
        val hasStorefront = state.businessStoreFrontUri != null
        VerificationCard(
            title = "Ảnh chụp địa điểm / Văn phòng",
            description = if (hasStorefront) "🎯 Đã tải lên ảnh thực tế" else "Ảnh chụp rõ bảng hiệu mặt tiền hoặc nơi làm việc",
            // ✅ ĐÃ SỬA: Ép kiểu logic Boolean về Enum VerificationStatus tương ứng
            status = if (hasStorefront) VerificationStatus.VERIFIED else VerificationStatus.UNVERIFIED,
            enabled = !state.verificationSubmitted,
            onClick = { storefrontPicker.launch("image/*") })

// ========================================
// MAP LOCATION (Vị trí doanh nghiệp)
// ========================================
        val hasLocation =
            state.businessLatitude != null && state.businessLongitude != null && state.businessLatitude != 0.0 && state.businessLongitude != 0.0
        VerificationCard(
            title = "Định vị địa chỉ trên Bản đồ",
            description = if (hasLocation) "📍 Đã ghim tọa độ thành công" else "Chọn vị trí để sinh viên dễ dàng tìm đường",
            // ✅ ĐÃ SỬA: Ép kiểu logic Boolean về Enum VerificationStatus tương ứng
            status = if (hasLocation) VerificationStatus.VERIFIED else VerificationStatus.UNVERIFIED,
            enabled = !state.verificationSubmitted,
            onClick = { navController.navigate("location_picker") })

        // ========================================
        // SUBMIT BUTTON (Gửi duyệt)
        // ========================================
        Button(
            onClick = { viewModel.submitVerification() },
            enabled = !state.isLoading && !state.verificationSubmitted,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB), // Xanh Blue chuyên nghiệp cho khối tuyển dụng
                disabledContainerColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = if (state.verificationSubmitted) "Hồ sơ đang chờ duyệt" else "Gửi yêu cầu xác thực",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
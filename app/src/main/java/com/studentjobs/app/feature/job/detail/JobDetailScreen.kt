package com.studentjobs.app.feature.job.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.application.apply.ApplyJobLimitType
import com.studentjobs.app.feature.application.apply.ApplyJobViewModel
import com.studentjobs.app.utils.dayOfWeekText
import com.studentjobs.app.utils.minuteToTime
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobDetailScreen(
    viewModel: JobDetailViewModel, applyViewModel: ApplyJobViewModel
) {
    val state by viewModel.uiState.collectAsState()

    val applyState by applyViewModel.uiState.collectAsState()

    val context = LocalContext.current

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val job = state.job ?: return

    LaunchedEffect(job.jobId) {
        applyViewModel.clearLimitType()

        applyViewModel.checkApplied(
            job.jobId
        )
    }

    LaunchedEffect(
        applyState.limitType
    ) {

        if (applyState.limitType == ApplyJobLimitType.ALREADY_APPLIED) {

            Toast.makeText(

                context,

                "Bạn đã ứng tuyển công việc này",

                Toast.LENGTH_SHORT

            ).show()

            applyViewModel.clearLimitType()
        }
    }

    if (applyState.limitType == ApplyJobLimitType.FREE_LIMIT_REACHED) {
        AlertDialog(

            onDismissRequest = {

                applyViewModel.clearLimitType()
            },

            title = {

                Text(
                    "Đạt giới hạn công việc"
                )
            },

            text = {

                Text(

                    "Bạn được quản lý tối đa 1 công việc cùng lúc.\n\n" +

                            "Hãy hoàn thành hoặc kết thúc công việc hiện tại trước khi ứng tuyển công việc mới."
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        applyViewModel.clearLimitType()
                    }) {

                    Text(
                        "Đã hiểu"
                    )
                }
            })
    }
    if (applyState.limitType == ApplyJobLimitType.PLUS_LIMIT_REACHED) {

        AlertDialog(

            onDismissRequest = {

                applyViewModel.clearLimitType()
            },

            title = {

                Text(
                    "Đạt giới hạn công việc"
                )
            },

            text = {

                Text(

                    "Bạn đang quản lý tối đa 2 công việc cùng lúc.\n\n" +

                            "Hãy hoàn thành hoặc kết thúc một công việc hiện tại trước khi ứng tuyển công việc mới."
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        applyViewModel.clearLimitType()
                    }) {

                    Text(
                        "Đã hiểu"
                    )
                }
            })
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // ========================================
            // 1. HERO HEADER (GRADIENT TRẺ TRUNG + LOGO)
            // ========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .padding(start = 20.dp, end = 20.dp, top = 32.dp, bottom = 48.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = job.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CardTravel,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = job.businessCategory,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Avatar/Logo đại diện cửa hàng (Tạo điểm nhấn thị giác)
                    Card(
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            // Lấy ký tự đầu làm logo tạm thời nếu chưa có ảnh
                            Text(
                                text = job.title.take(1).uppercase(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            // ========================================
            // 2. OVERLAP CARD (KHỐI THÔNG TIN NỔI LƠ LỬNG)
            // ========================================
            val formattedSalary = if (job.salaryMin == job.salaryMax) {
                "${formatCurrency(job.salaryMin)}đ/giờ"
            } else {
                "${formatCurrency(job.salaryMin)} - ${formatCurrency(job.salaryMax)}đ/giờ"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-24).dp), // Kỹ thuật đẩy lùi margin âm tạo chiều sâu 3D
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Phần hiển thị lương được làm nổi bật với tone màu ấm áp
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CurrencyExchange,
                            contentDescription = null,
                            tint = Color(0xFFFF9800), // Màu cam năng động
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Mức lương đề xuất",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Text(
                                text = formattedSalary,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF9800)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // Phần hiển thị địa điểm công việc
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Địa điểm làm việc",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Text(
                                text = job.locationText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2f
                            )
                        }
                    }
                }
            }

            // Phần thân chứa các chi tiết khác (Dạng Minimalist phẳng, thoáng đãng)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // ========================================
                // MÔ TẢ CÔNG VIỆC
                // ========================================
                SectionTitle(text = "Mô tả công việc")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = job.description,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.35f,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ========================================
                // KỸ NĂNG YÊU CẦU
                // ========================================
                SectionTitle(text = "Kỹ năng cần có")
                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    job.requiredSkills.forEach { skill ->
                        Surface(
                            shape = CircleShape, // Kiểu viên thuốc trẻ trung năng động
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ) {
                            Text(
                                text = skill,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ========================================
                // CA LÀM VIỆC
                // ========================================
                SectionTitle(text = "Lịch ca làm việc trong tuần")
                Spacer(modifier = Modifier.height(12.dp))

                state.shifts.forEach { shift ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = dayOfWeekText(
                                            shift.dayOfWeek
                                        ),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${
                                            minuteToTime(
                                                shift.startMinute
                                            )
                                        } - ${
                                            minuteToTime(
                                                shift.endMinute
                                            )
                                        }",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Badge hiển thị số lượng chỗ còn trống trống sạch sẽ
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 1.dp
                            ) {}
                        }
                    }
                }

                // Tạo khoảng trống đệm ở cuối danh sách cuộn tránh bị nút ghim đè chữ
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // ========================================
        // STICKY BOTTOM BAR (NÚT ỨNG TUYỂN CỐ ĐỊNH)
        // ========================================
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            tonalElevation = 8.dp,
            shadowElevation = 16.dp
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(
                        start = 20.dp, end = 20.dp, top = 12.dp, bottom = 20.dp
                    ) // padding bottom sâu hơn tí cho cân đối máy tai thỏ/nút home ảo
            ) {
                Button(

                    onClick = {

                        applyViewModel.applyJob(
                            job.jobId
                        )
                    },

                    enabled = !applyState.isLoading && !applyState.hasApplied,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),

                    shape = RoundedCornerShape(16.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {

                    Text(

                        text = when {

                            applyState.isLoading ->

                                "Đang gửi..."

                            applyState.hasApplied ->

                                "Đã ứng tuyển"

                            else ->

                                "Ứng tuyển ngay"
                        },

                        style = MaterialTheme.typography.titleMedium,

                        fontWeight = FontWeight.Bold,

                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

// Thành phần tiêu đề phân đoạn
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.onSurface
    )
}

// Định dạng tiền tệ VNĐ chuẩn phân cách dấu chấm (ví dụ: 30.000)
private fun formatCurrency(amount: Double): String {
    return try {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        formatter.format(amount.toLong())
    } catch (e: Exception) {
        amount.toInt().toString()
    }
}
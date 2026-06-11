package com.studentjobs.app.feature.job.employer

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studentjobs.app.data.model.job.JobEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerJobScreen(
    navController: NavController,
    viewModel: EmployerJobViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {

        viewModel.loadData()
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    // LỌC DANH SÁCH THEO TRẠNG THÁI (Anh có thể đổi điều kiện mapping tùy theo Model thực tế)
    val recruitingJobs = uiState.jobs.filter { it.status.uppercase() == "ACTIVE" }
    val ongoingJobs = uiState.jobs.filter { it.status.uppercase() == "ON_GOING" }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Quản lý tuyển dụng",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Số vị trí đang mở",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                    Text(
                        text = "${uiState.activeJobCount}/${uiState.maxJobAllowed}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { uiState.activeJobCount.toFloat() / uiState.maxJobAllowed.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // =========================
            // BUTTON TẠO JOB MỚI
            // =========================
            if (uiState.activeJobCount < uiState.maxJobAllowed) {
                item {
                    Button(
                        onClick = { navController.navigate("create_job") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Đăng tin tuyển dụng mới",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold, fontSize = 15.sp
                            )
                        )
                    }
                }
            }

            // =================================================================================
            // PHÂN VÙNG 1: ĐANG TUYỂN DỤNG
            // =================================================================================
            item {
                Text(
                    text = "Đang tuyển dụng (${recruitingJobs.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            if (recruitingJobs.isEmpty()) {
                item {
                    EmptySectionState(
                        message = "Chưa có công việc cần tuyển dụng...",
                        icon = Icons.Default.WorkOutline
                    )
                }
            } else {
                items(recruitingJobs, key = { it.jobId }) { job ->
                    JobItemCard(job = job, navController = navController, isActive = true)
                }
            }

            // =================================================================================
            // PHÂN VÙNG 2: ĐANG HOẠT ĐỘNG
            // =================================================================================
            item {
                Text(
                    text = "Đang hoạt động (${ongoingJobs.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }

            if (ongoingJobs.isEmpty()) {
                item {
                    EmptySectionState(
                        message = "Chưa có công việc đang hoạt động...",
                        icon = Icons.Default.AssignmentTurnedIn
                    )
                }
            } else {
                items(ongoingJobs, key = { it.jobId }) { job ->
                    JobItemCard(job = job, navController = navController, isActive = false)
                }
            }

            // Khoảng trống nghệ thuật cuối màn hình
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

// =================================================================================
// COMPOSABLE SUB-VIEWS ĐỂ CODE SẠCH SẼ, DỄ CHẤM ĐIỂM TIỂU LUẬN
// =================================================================================

@Composable
fun JobItemCard(
    job: JobEntity,
    navController: NavController,
    isActive: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // Hiệu ứng tự động co giãn cực kỳ mượt mà khi đổi trạng thái
            .clickable {

                if (isActive) {

                    navController.navigate(
                        "employer_job_detail/${job.jobId}"
                    )

                } else {

                    navController.navigate(
                        "active_job_detail/${job.jobId}"
                    )
                }
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f) // Đổi tone nhẹ khi Job đang chạy
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold, fontSize = 18.sp
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Badge Trạng thái
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isActive) Color(0xFFE8F5E9) else Color(0xFFE8EAF6))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isActive) Color(0xFF4CAF50) else Color(0xFF3F51B5))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isActive) "Đang tuyển" else "Đang chạy",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isActive) Color(0xFF2E7D32) else Color(0xFF3F51B5),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = if (isActive && job.currentApplicants > 0) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isActive) "${job.currentApplicants} ứng viên mới" else "Sinh viên đang làm việc",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isActive && job.currentApplicants > 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (isActive && job.currentApplicants > 0) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    )
                }

                Text(
                    text = "Cần tuyển: ${job.requiredApplicants}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun EmptySectionState(
    message: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
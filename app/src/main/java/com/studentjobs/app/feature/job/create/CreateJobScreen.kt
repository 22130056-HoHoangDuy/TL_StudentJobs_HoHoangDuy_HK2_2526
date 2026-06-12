package com.studentjobs.app.feature.job.create

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.studentjobs.app.feature.job.create.components.BasicInfoSection
import com.studentjobs.app.feature.job.create.components.JobPreviewCard
import com.studentjobs.app.feature.job.create.components.SalarySection
import com.studentjobs.app.feature.job.create.components.ShiftSection
import com.studentjobs.app.feature.job.create.components.SkillSelectorSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobScreen(
    employerBusinessName: String,
    viewModel: CreateJobViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onJobCreated: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.success) {
        if (state.success) {
            onJobCreated()
        }
    }

    val auth = FirebaseAuth.getInstance()
    val employerUid = auth.currentUser?.uid

    // Bảng màu mới: Tập trung hoàn toàn vào Gradient nền rực rỡ, bỏ hoàn toàn Border
    val infoGradient = listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))    // Tím/Xanh mộng mơ
    val financeGradient = listOf(Color(0xFFD1FAE5), Color(0xFFA7F3D0)) // Xanh Mint tài lộc
    val shiftGradient = listOf(Color(0xFFDBEAFE), Color(0xFFBFDBFE))   // Xanh Da trời năng động

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Tạo công việc mới",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .navigationBarsPadding()
                ) {
                    Button(
                        onClick = { employerUid?.let { viewModel.createJob(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Icon(Icons.Default.RocketLaunch, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Đăng tin tuyển dụng",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. PREVIEW SECTION
            item {
                SectionHeader("Xem trước tin đăng", "Cách sinh viên nhìn thấy bài đăng của bạn")
                JobPreviewCard(
                    title = state.title.ifBlank { "Tên công việc sẽ hiện ở đây" },
                    salaryMin = state.salaryMin,
                    salaryMax = state.salaryMax,
                    businessName = employerBusinessName,
                    selectedSkillCount = state.selectedSkills.size
                )
            }

            // 2. BASIC INFO
            item {
                SectionWrapper(bgGradient = infoGradient) {
                    BasicInfoSection(
                        title = state.title,
                        description = state.description,
                        requiredApplicants = state.requiredApplicants,
                        onTitleChange = viewModel::updateTitle,
                        onDescriptionChange = viewModel::updateDescription,
                        onApplicantsChange = viewModel::updateRequiredApplicants
                    )
                }
            }

            // 3. SALARY & SKILLS
            item {
                SectionWrapper(bgGradient = financeGradient) {
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        SalarySection(
                            salaryMin = state.salaryMin,
                            salaryMax = state.salaryMax,
                            onSalaryMinChange = viewModel::updateSalaryMin,
                            onSalaryMaxChange = viewModel::updateSalaryMax
                        )
                        Divider(color = Color.White.copy(alpha = 0.6f))
                        SkillSelectorSection(
                            availableSkills = state.availableSkills,
                            selectedSkills = state.selectedSkills,
                            onSkillToggle = viewModel::toggleSkill
                        )
                    }
                }
            }

            // 4. SHIFTS
            item {
                SectionWrapper(bgGradient = shiftGradient) {
                    ShiftSection(
                        shifts = state.shifts,
                        selectedDay = state.selectedDay,
                        startMinute = state.startMinute,
                        endMinute = state.endMinute,
                        slots = state.slots,
                        onDaySelected = viewModel::updateSelectedDay,
                        onStartMinuteChange = viewModel::updateStartMinute,
                        onEndMinuteChange = viewModel::updateEndMinute,
                        onAddShift = viewModel::addShift,
                        onDeleteShift = viewModel::removeShift
                    )
                }
            }

            // 5. SMART AUTO RECRUITMENT
            item {
                val plusGradient = Brush.linearGradient(
                    colors = listOf(Color(0xFF6366F1), Color(0xFFA855F7))
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFF8B5CF6)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Smart Auto Recruitment",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Text(
                            "Tự động tìm kiếm và mời các sinh viên có TrustScore cao phù hợp nhất.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        if (state.isPlusEmployer) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.5f))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Kích hoạt tự động", fontWeight = FontWeight.Medium)
                                Switch(
                                    checked = state.autoRecruitmentEnabled,
                                    onCheckedChange = { viewModel.toggleAutoRecruitment() }
                                )
                            }
                        } else {
                            Button(
                                onClick = onNavigateToSubscription,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(plusGradient),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "Nâng cấp PLUS để mở khóa",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Messages Section
            item {
                AnimatedVisibility(visible = state.errorMessage != null) {
                    Text(
                        state.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
                AnimatedVisibility(visible = state.success) {
                    Text(
                        "✅ Tạo công việc thành công!",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

// Cập nhật lại SectionWrapper: Loại bỏ Border, sử dụng nền Gradient lấp đầy trọn vẹn khối
@Composable
fun SectionWrapper(
    bgGradient: List<Color>,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // Nền Transparent để lộ Box Gradient
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.linearGradient(colors = bgGradient)) // Tô trọn màu nền
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}
package com.studentjobs.app.feature.skill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.skill.BusinessCategory
import com.studentjobs.app.data.model.skill.SkillCatalog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ManageSkillsScreen(
    currentCategories: List<String>,
    currentSkills: List<String>,
    isPlus: Boolean,
    onSave: (List<String>, List<String>) -> Unit
) {
    var selectedCategories by remember { mutableStateOf(currentCategories.toMutableList()) }
    var selectedSkills by remember { mutableStateOf(currentSkills.toMutableList()) }

    val maxCategories = if (isPlus) 5 else 2
    val maxSkills = 10

    val availableSkills = selectedCategories.flatMap {
        SkillCatalog.getSkillsByCategory(it)
    }.distinctBy { it.skillName }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quản lý Kỹ năng",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8FAFC) // Đồng bộ với nền sáng tinh tế của MainScreen
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ==================================
            // BANNER GỢI Ý ĐẶC QUYỀN PLUS TÀI KHOẢN
            // ==================================
            if (!isPlus) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFEF3C7) // Màu vàng kem nhẹ nhàng dễ chịu
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "⭐ Mẹo nhỏ Hội viên PLUS",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tài khoản thường chỉ được chọn tối đa 2 ngành nghề. Nâng cấp tài khoản PLUS để mở rộng lên 5 ngành nghề cùng lúc nhé!",
                            color = Color(0xFFB45309),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ==================================
            // SECTIONS 1: NGÀNH NGHỀ QUAN TÂM
            // ==================================
            Text(
                text = "Ngành nghề quan tâm",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Đã chọn ${selectedCategories.size} trên tối đa $maxCategories mục",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BusinessCategory.all.forEach { category ->
                    val selected = selectedCategories.contains(category)

                    FilterChip(
                        selected = selected,
                        onClick = {
                            if (selected) {
                                selectedCategories = selectedCategories.toMutableList().apply {
                                    remove(category)
                                }
                                // Khử bớt những kỹ năng thuộc ngành nghề vừa bị xóa
                                val remainingSkills =
                                    SkillCatalog.getSkillsByCategory(category).map { it.skillName }
                                selectedSkills = selectedSkills.toMutableList().apply {
                                    removeAll(remainingSkills)
                                }
                            } else {
                                if (selectedCategories.size < maxCategories) {
                                    selectedCategories = selectedCategories.toMutableList().apply {
                                        add(category)
                                    }
                                }
                            }
                        },
                        label = {
                            Text(
                                text = category,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,            // Đổi từ unselectedContainerColor
                            labelColor = Color(0xFF475569),          // Đổi từ unselectedLabelColor
                            selectedContainerColor = Color(0xFF2563EB),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ==================================
            // SECTIONS 2: KỸ NĂNG CỦA BẠN
            // ==================================
            Text(
                text = "Kỹ năng chuyên môn",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Đã chọn ${selectedSkills.size} trên tối đa $maxSkills mục (Chọn ngành nghề phía trên để hiển thị kỹ năng tương ứng)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (availableSkills.isEmpty()) {
                Text(
                    text = "Vui lòng tick chọn ít nhất một ngành nghề phía trên để hệ thống hiển thị bộ kỹ năng gợi ý.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray.copy(alpha = 0.8f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableSkills.forEach { skill ->
                        val selected = selectedSkills.contains(skill.skillName)

                        FilterChip(
                            selected = selected,
                            onClick = {
                                if (selected) {
                                    selectedSkills = selectedSkills.toMutableList().apply {
                                        remove(skill.skillName)
                                    }
                                } else {
                                    if (selectedSkills.size < maxSkills) {
                                        selectedSkills = selectedSkills.toMutableList().apply {
                                            add(skill.skillName)
                                        }
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = skill.skillName,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,            // Đổi từ unselectedContainerColor
                                labelColor = Color(0xFF475569),          // Đổi từ unselectedLabelColor
                                selectedContainerColor = Color(0xFF7C3AED),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ==================================
            // NÚT LƯU THAY ĐỔI
            // ==================================
            val isSaveEnabled = selectedCategories.isNotEmpty()

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = isSaveEnabled,
                shape = RoundedCornerShape(14.dp),
                onClick = {
                    onSave(selectedCategories, selectedSkills)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0F172A), // Màu xanh thẫm quyền lực
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFE2E8F0),
                    disabledContentColor = Color(0xFF94A3B8)
                )
            ) {
                Text(
                    text = "Lưu Thay Đổi",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
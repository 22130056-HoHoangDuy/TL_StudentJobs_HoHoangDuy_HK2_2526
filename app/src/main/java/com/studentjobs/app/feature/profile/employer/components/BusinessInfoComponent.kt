package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.data.model.skill.BusinessCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessInfoComponent(
    isEditable: Boolean = false,
    businessName: String,
    businessCategory: String,
    businessAddress: String,
    businessDesc: String,
    businessUrl: String,
    onEditClick: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onAddressChange: (String) -> Unit = {},
    onDescChange: (String) -> Unit = {},
    onUrlChange: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Thông tin doanh nghiệp",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                // Nút Edit chỉ hiện khi không ở chế độ sửa và không phải là chế độ form
                if (!isEditable) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
                            .size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            null,
                            tint = Color(0xFF06B6D4),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isEditable) {
                // Form nhập liệu
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF06B6D4),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF06B6D4)
                )

                OutlinedTextField(
                    value = businessName,
                    onValueChange = onNameChange,
                    label = { Text("Tên doanh nghiệp") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                //Employer chose type of business
                var expanded by remember {
                    mutableStateOf(false)
                }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(
                        value = businessCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Lĩnh vực")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = textFieldColors
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        BusinessCategory.all.forEach { category ->

                            DropdownMenuItem(
                                text = {
                                    Text(category)
                                },
                                onClick = {

                                    onCategoryChange(category)

                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = businessAddress,
                    onValueChange = onAddressChange,
                    label = { Text("Địa chỉ") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))
                // save gg map share link
                OutlinedTextField(
                    value = businessUrl,
                    onValueChange = onUrlChange,
                    label = {
                        Text("Link Google Maps")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = businessDesc,
                    onValueChange = onDescChange,
                    label = { Text("Giới thiệu") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    colors = textFieldColors
                )
            } else {
                // View Mode (Bento)
                BentoInfoRow(Icons.Default.Business, "Tên công ty", businessName)
                BentoInfoRow(Icons.Default.Category, "Lĩnh vực", businessCategory)
                BentoInfoRow(Icons.Default.LocationOn, "Địa chỉ", businessAddress)
                BentoInfoRow(Icons.Default.Description, "Giới thiệu", businessDesc)
            }
        }
    }
}

@Composable
fun BentoInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier, // Đã thêm dấu phẩy và giá trị mặc định
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFF06B6D4), modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
            Text(
                value.ifBlank { "Chưa cập nhật" },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
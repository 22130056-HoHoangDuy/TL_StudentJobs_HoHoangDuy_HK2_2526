package com.studentjobs.app.feature.trust.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentjobs.app.feature.trust.TrustFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrustFilterDropdown(
    selectedFilter: TrustFilter,
    onFilterChange: (TrustFilter) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 4.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = when (selectedFilter) {
                    TrustFilter.ALL -> "Tất cả biến động"
                    TrustFilter.POSITIVE -> "Điểm cộng (+)"
                    TrustFilter.NEGATIVE -> "Điểm trừ (-)"
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD946EF),
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Tất cả biến động", fontWeight = FontWeight.Medium) },
                    onClick = { onFilterChange(TrustFilter.ALL); expanded = false }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Điểm cộng (+)",
                            color = Color(0xFF22C55E),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onClick = { onFilterChange(TrustFilter.POSITIVE); expanded = false }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Điểm trừ (-)",
                            color = Color(0xFFEF4444),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onClick = { onFilterChange(TrustFilter.NEGATIVE); expanded = false }
                )
            }
        }
    }
}
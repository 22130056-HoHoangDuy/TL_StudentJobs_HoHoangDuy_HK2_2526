package com.studentjobs.app.feature.job.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicInfoSection(
    title: String,
    description: String,
    requiredApplicants: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onApplicantsChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📋 Thông tin công việc",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Tiêu đề công việc") },
                placeholder = { Text("Ví dụ: Phục vụ bàn bán thời gian") }
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                label = { Text("Mô tả công việc") },
                placeholder = { Text("Mô tả chi tiết công việc, quyền lợi và yêu cầu...") }
            )

            OutlinedTextField(
                value = requiredApplicants,
                onValueChange = onApplicantsChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Số lượng tuyển dụng") },
                placeholder = { Text("Ví dụ: 2") }
            )
        }
    }
}
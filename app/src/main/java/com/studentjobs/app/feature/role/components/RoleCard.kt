package com.studentjobs.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RoleCard(
    title: String,
    description: String,
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val bgColor = if (selected) Color.White else Color.White.copy(alpha = 0.2f)

    val textColor = if (selected) Color.Black else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(bgColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            icon()

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(title, color = textColor, style = MaterialTheme.typography.titleMedium)
                Text(description, color = textColor.copy(alpha = 0.7f))
            }
        }
    }
}
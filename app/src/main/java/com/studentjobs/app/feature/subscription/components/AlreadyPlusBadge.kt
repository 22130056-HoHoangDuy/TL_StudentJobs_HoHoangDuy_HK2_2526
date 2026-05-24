package com.studentjobs.app.feature.subscription.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AlreadyPlusBadge() {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFE8F5E9),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(

            imageVector =
                Icons.Default.CheckCircle,

            contentDescription = null,

            tint = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(

            text = "Bạn đang sử dụng PLUS",

            fontWeight = FontWeight.Bold,

            color = Color(0xFF2E7D32)
        )
    }
}
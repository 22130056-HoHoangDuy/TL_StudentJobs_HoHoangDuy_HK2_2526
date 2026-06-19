package com.studentjobs.app.feature.profile.employer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.ProfileUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun EmployerLocationCard(
    state: ProfileUiState,
    onSelectLocation: () -> Unit
) {
    val context = LocalContext.current
    // 🔥 ĐIỂM SỬA CHỮA QUAN TRỌNG: Lấy đúng employerProfile
    val employer = state.employerProfile

    var displayAddress by remember { mutableStateOf("Đang xác định vị trí...") }

    // Trong EmployerLocationCard.kt
    LaunchedEffect(employer?.businessLatitude, employer?.businessLongitude) {
        val lat = employer?.businessLatitude
        val lng = employer?.businessLongitude

        if (lat != null && lng != null) {
            displayAddress = withContext(Dispatchers.IO) {
                // Gọi qua object dùng chung thay vì hàm cục bộ
                com.studentjobs.app.utils.LocationUtils.getReadableAddress(context, lat, lng)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF1F5F9) // Màu xám nhẹ cho Employer
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF06B6D4))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Vị trí doanh nghiệp", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hiển thị địa chỉ đã dịch
            Text(
                text = if (employer?.businessLatitude != null) displayAddress else "Chưa thiết lập vị trí",
                style = MaterialTheme.typography.bodyLarge
            )

            TextButton(onClick = onSelectLocation) {
                Text(if (employer?.businessLatitude == null) "Chọn vị trí" else "Thay đổi")
            }
        }
    }
}
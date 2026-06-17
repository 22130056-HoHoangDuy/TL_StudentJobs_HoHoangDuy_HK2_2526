package com.studentjobs.app.feature.profile.student.components

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studentjobs.app.feature.profile.ProfileUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun LocationInfoCard(
    state: ProfileUiState,
    onSelectLocation: () -> Unit,
    onOpenGoogleMaps: () -> Unit = {}
) {
    val context = LocalContext.current
    val profile = state.studentProfile

    // State lưu địa chỉ dạng chữ (Phường, Quận...) để hiển thị lên UI
    var displayAddress by remember { mutableStateOf("Đang xác định vị trí...") }

    // Tự động chạy ngầm dịch tọa độ sang địa chỉ mỗi khi Lat/Lng thay đổi
    LaunchedEffect(profile?.studentLatitude, profile?.studentLongitude) {
        val lat = profile?.studentLatitude
        val lng = profile?.studentLongitude

        if (lat != null && lng != null) {
            displayAddress = withContext(Dispatchers.IO) {
                getReadableAddress(context, lat, lng)
            }
        }
    }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E1B4B)
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .padding(22.dp)
        ) {

            // ===== HEADER =====
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF06B6D4)
                )

                Text(
                    text = "Khu vực làm việc",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TRƯỜNG HỢP 1: Sinh viên chưa chọn vị trí
            if (profile?.studentLatitude == null || profile.studentLongitude == null) {
                Text(
                    text = "Bạn chưa chọn vị trí để tìm việc gần đây rồi...",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSelectLocation,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF06B6D4)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Chọn Vị Trí Ngay", color = Color.White, fontWeight = FontWeight.Bold)
                }

            } else {
                // TRƯỜNG HỢP 2: Đã định vị thành công
                Text(
                    text = "Địa chỉ tìm việc hiện tại",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = displayAddress,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onSelectLocation,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF334155)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Thay Đổi Vị Trí", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Bấm nút này sẽ tự động kích hoạt mở Google Maps luôn!
                    TextButton(
                        onClick = {
                            // Gọi callback truyền từ bên ngoài (nếu có)
                            onOpenGoogleMaps()

                            // Thực hiện mở Google Maps trực tiếp bằng Intent ngay tại đây
                            val lat = profile.studentLatitude
                            val lng = profile.studentLongitude

                            val mapUri =
                                Uri.parse("geo:$lat,$lng?q=$lat,$lng(Vị trí tìm việc của tôi)")
                            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri).apply {
                                setPackage("com.google.android.apps.maps")
                            }

                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
                                )
                                context.startActivity(browserIntent)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Xem Bản Đồ",
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Hàm Geocoder dịch ngược tọa độ sang chữ
 */
private fun getReadableAddress(context: Context, lat: Double, lng: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale("vi", "VN"))
        val addresses = geocoder.getFromLocation(lat, lng, 1)

        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val ward = address.subLocality
            val district = address.subAdminArea
            val city = address.locality

            val result = StringBuilder()

            if (!ward.isNullOrBlank()) result.append(ward)
            if (!district.isNullOrBlank()) {
                if (result.isNotEmpty()) result.append(", ")
                result.append(district)
            }

            if (result.isEmpty() && !city.isNullOrBlank()) {
                result.append(city)
            }

            if (result.isNotEmpty()) result.toString() else "Tọa độ: $lat, $lng"
        } else {
            "Vị trí chưa được xác định"
        }
    } catch (e: Exception) {
        "Tọa độ: $lat, $lng"
    }
}